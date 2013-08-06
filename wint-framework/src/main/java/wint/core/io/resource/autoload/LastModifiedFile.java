package wint.core.io.resource.autoload;

import java.io.File;

import wint.core.io.resource.Resource;

public class LastModifiedFile {

	private long lastModified;
	
	private File file;

	public LastModifiedFile(Resource resource) {
		this.file = resource.getFile();
		if (file != null) {
			this.lastModified = file.lastModified();
		}
	}
	
	public LastModifiedFile(long lastModified, File file) {
		super();
		this.lastModified = lastModified;
		this.file = file;
	}

	public long getLastModified() {
		return lastModified;
	}
	
	public boolean hasModified() {
		if (file == null) {
			return false;
		}
		return file.lastModified() > lastModified;
	}
	
	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
	
}
