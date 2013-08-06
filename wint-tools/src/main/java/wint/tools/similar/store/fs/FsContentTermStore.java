package wint.tools.similar.store.fs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import wint.tools.similar.proccess.ContentTerm;
import wint.tools.similar.store.ContentTermStore;
import wint.tools.similar.store.SimilarTermSerializeService;
import wint.tools.util.CloseableIterator;
import wint.tools.util.CloseableIteratorWrapper;
import wint.tools.util.IoUtil;

public class FsContentTermStore implements ContentTermStore {

	private File basePath;
	
	private int filesPerGroup = 128;
	
	private SimilarTermSerializeService similarTermSerializeService = new SimilarTermSerializeService();
	
	public FsContentTermStore(File basePath) {
		super();
		this.basePath = basePath;
	}

	public void save(String groupId, String id, ContentTerm contentTerm) {
		File file = getTargetFile(groupId, id);
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			similarTermSerializeService.serializeContentTerm(contentTerm, fos);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} finally {
			IoUtil.close(fos);
		}
	}
	
	private File getTargetFile(String groupId, String id) {
		File groupFile = new File(basePath, groupId);
		int value = Math.abs(id.hashCode()) % filesPerGroup;
		File targetFileDir = new File(groupFile, String.valueOf(value));
		targetFileDir.mkdirs();
		return new File(targetFileDir, id);
	}

	public int getContentTermCount(String groupId) {
		File groupFile = new File(basePath, groupId);
		if (!groupFile.exists()) {
			return 0;
		}
		return childFileCount(groupFile);
	}
	
	private int childFileCount(File path) {
		File[] files = path.listFiles();
		int count = 0;
		for (File file : files) {
			if (file.isFile()) {
				count ++;
			} else if (file.isDirectory()) {
				count += childFileCount(file);
			}
		}
		return count;
	}

	public CloseableIterator<ContentTerm> getContentTerms(String groupId) {
		return new CloseableIteratorWrapper<ContentTerm>(new FsContentTermIterator(similarTermSerializeService, new File(basePath, groupId)));
	}

}
