package wint.tools.similar.content;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;

import wint.tools.util.CloseableIterator;
import wint.tools.util.CloseableIteratorWrapper;
import wint.tools.util.CollectionUtil;

public class FsContentResource implements ContentResource {

	private File path;
	
	private boolean recurse = false;
	
	private String extern = ".txt";
	
	public FsContentResource(File path) {
		super();
		this.path = path;
	}
	public FsContentResource(String path) {
		this(new File(path));
	}

	public CloseableIterator<Content> getContents(String groupId) {
		List<Content> contents = getContentsImpl(path);
		return new CloseableIteratorWrapper<Content>(contents.iterator());
	}
	
	private List<Content> getContentsImpl(File dir) {
		File[] files = dir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return match(dir, name);
			}
		});
		List<Content> ret = CollectionUtil.newArrayList();
		for (File file : files) {
			if (file.isDirectory()) {
				if (recurse) {
					ret.addAll(getContentsImpl(file));
				}
			} else if (file.isFile()) {
				ret.add(new FileContent(file));
			}
		}
		return ret;
	}
	
	protected boolean match(File dir, String filename) {
		if (dir.isDirectory()) {
			return true;
		}
		return filename.endsWith(extern);
	}

	public File getPath() {
		return path;
	}

	public void setPath(File path) {
		this.path = path;
	}

	public boolean isRecurse() {
		return recurse;
	}

	public void setRecurse(boolean recurse) {
		this.recurse = recurse;
	}

	public String getExtern() {
		return extern;
	}

	public void setExtern(String extern) {
		this.extern = extern;
	}

}
