package wint.core.io.resource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import wint.core.io.resource.loader.ResourceLoader;
import wint.lang.exceptions.ResourceException;

public class URLResource extends AbstractResource {

	private URL url;
	
	private ResourceLoader resourceLoader;
	
	public URLResource(URL url, ResourceLoader resourceLoader) {
		this.url = url;
		this.resourceLoader = resourceLoader;
	}
	
	public Resource createRelative(String relativePath) {
		return resourceLoader.getResource(relativePath);
	}

	public File getFile() {
		try {
			return ResourceUtil.getFile(url);
		} catch (FileNotFoundException e) {
			throw new ResourceException(e);
		}
	}

	public InputStream getInputStream() throws IOException {
		URLConnection con = this.url.openConnection();
		con.setUseCaches(false);
		return con.getInputStream();
	}

	public String getDescription() {
		return "URL [" + this.url + "]";
	}

	public URL getURL() {
		return url;
	}

}
