package wint.core.io.resource.loader;

import java.net.URL;

import wint.core.io.resource.NotExistResource;
import wint.core.io.resource.Resource;
import wint.core.io.resource.URLResource;

public class DefaultResourceLoader implements ResourceLoader {

	public Resource getResource(String name) {
		URL url = DefaultResourceLoader.class.getResource(name);
		if (url != null) {
			return new URLResource(url, this);
		}
		ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
		if (contextClassLoader != null) {
			url = contextClassLoader.getResource(name);
			if (url != null) {
				return new URLResource(url, this);
			}
		}
		url = DefaultResourceLoader.class.getClassLoader().getResource(name);
		if (url != null) {
			return new URLResource(url, this);
		}
		return new NotExistResource();
	}

}
