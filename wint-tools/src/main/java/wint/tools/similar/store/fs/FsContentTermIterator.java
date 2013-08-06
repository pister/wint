package wint.tools.similar.store.fs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;

import wint.tools.similar.proccess.ContentTerm;
import wint.tools.similar.store.SimilarTermSerializeService;
import wint.tools.util.IoUtil;

/**
 * 2012-8-22 下午8:24:59
 */
// TODO
public class FsContentTermIterator implements Iterator<ContentTerm> {

	private SimilarTermSerializeService similarTermSerializeService;
	
	private File basePath;
	
	private boolean moreElements = true;
	
	private LinkedBlockingQueue<FileEntry> filesQueue = new LinkedBlockingQueue<FileEntry>(128);
	
	public FsContentTermIterator(SimilarTermSerializeService similarTermSerializeService, File basePath) {
		super();
		this.similarTermSerializeService = similarTermSerializeService;
		this.basePath = basePath;
		
		new AddFileThread().start();
	}
	
	private File targetFile; 

	public boolean hasNext() {
		if (!moreElements) {
			return false;
		}
		FileEntry fileEntry = null;
		try {
			fileEntry = filesQueue.take();
			if (fileEntry.eof) {
				moreElements = false;
				return false;
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			return false;
		}
		targetFile =  fileEntry.file;
		return true;
	}

	public ContentTerm next() {
		File file =  targetFile;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			return similarTermSerializeService.deserializeContentTerm(fis);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} finally {
			IoUtil.close(fis);
		}
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

	static class FileEntry {
		File file;
		boolean eof;
		public FileEntry(File file, boolean eof) {
			super();
			this.file = file;
			this.eof = eof;
		}
		static FileEntry EOF = new FileEntry(null, true);
	}
	
	class AddFileThread extends Thread {
		
		public void run() {
			listAllFiles(basePath);
			try {
				filesQueue.put(FileEntry.EOF);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
		
		private void listAllFiles(File file) {
			File[] files = file.listFiles();
			if (files == null || files.length == 0) {
				return;
			}
			for (File childFile : files) {
				if (childFile.isFile()) {
					try {
						filesQueue.put(new FileEntry(childFile, false));
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				} else if (childFile.isDirectory()) {
					listAllFiles(childFile);
				}
			}
			
		}
	}
	
}
