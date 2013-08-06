package wint.core.io.resource.loader;

import java.io.IOException;
import java.net.URL;

import javax.servlet.ServletContext;

import wint.core.io.resource.Resource;
import wint.core.io.resource.URLResource;
import wint.lang.exceptions.ResourceException;
import wint.lang.utils.FileUtil;

public class WebAppResourceLoader implements ResourceLoader {

	private ResourceLoader defaultResourceLoader = new DefaultResourceLoader();
	
	private ServletContext servletContext;
	
	public WebAppResourceLoader(ServletContext servletContext) {
		super();
		this.servletContext = servletContext;
	}

	public Resource getResource(String name) {
		Resource resource = loadWebResource(name);
		if (resource != null) {
			return resource;
		}
		return defaultResourceLoader.getResource(name);
	}
	
	protected Resource loadWebResource(String name) {
		URL url = WebAppResourceLoader.class.getResource("/WEB-INF/" + name);
		if (url != null) {
			return new URLResource(url, this);
		}
		url = WebAppResourceLoader.class.getResource("/WEB-INF/classes/" + name);
		if (url != null) {
			return new URLResource(url, this);
		}
		try {
			String realPath = getRealPathFromServlet(name);
			if (realPath != null) {
				url = new URL("file:///" + realPath);
			}
			if (url != null) {
				return new URLResource(url, this);
			}
		} catch (IOException e) {
			throw new ResourceException(e);
		}
		return null;
	}

	private String getRealPathFromServlet(String name) throws IOException {
		if (!name.startsWith("/")) {
			name = "/" + name;
		}
		
		String realPath = servletContext.getRealPath(name);
		if (realPath != null && FileUtil.exist(realPath)) {
			return realPath;
		}	
				
		realPath = servletContext.getRealPath("/WEB-INF" + name);
		if (realPath != null && FileUtil.exist(realPath)) {
			return realPath;
		}
		
		realPath = servletContext.getRealPath("/WEB-INF/classes" + name);
		if (realPath != null && FileUtil.exist(realPath)) {
			return realPath;
		}
		return null;
	}
	
	
	
}
