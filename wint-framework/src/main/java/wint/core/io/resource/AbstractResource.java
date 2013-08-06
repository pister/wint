package wint.core.io.resource;

import wint.lang.exceptions.ResourceException;

public abstract class AbstractResource implements Resource {

	public boolean exist() {
		try {
			return getFile().exists();
		}catch (Exception ex) {
			return false;
		}
	}

	public boolean isOpen() {
		return false;
	}
	
	public String toString() {
		return getDescription();
	}

	public long lastModified() {
		long lastModified = getFile().lastModified();
		if (lastModified == 0L) {
			throw new ResourceException(getDescription() +
					" cannot be resolved in the file system for resolving its last-modified timestamp");
		}
		return lastModified;
	}

}
