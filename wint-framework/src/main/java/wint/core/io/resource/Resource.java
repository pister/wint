package wint.core.io.resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public interface Resource {

	InputStream getInputStream() throws IOException;
	
	boolean exist();
	
	String getDescription();
	
	File getFile();
	
	URL getURL();
	
	boolean isOpen();
	
	long lastModified();
	
	Resource createRelative(String relativePath);
	
}
