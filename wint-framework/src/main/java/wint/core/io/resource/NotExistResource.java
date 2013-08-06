package wint.core.io.resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class NotExistResource implements Resource {

	public Resource createRelative(String relativePath) {
		return null;
	}

	public String getDescription() {
		return null;
	}

	public File getFile() {
		return null;
	}

	public InputStream getInputStream() throws IOException {
		return null;
	}

	public URL getURL() {
		return null;
	}

	public boolean exist() {
		return false;
	}

	public boolean isOpen() {
		return false;
	}

	public long lastModified() {
		return 0;
	}

}
