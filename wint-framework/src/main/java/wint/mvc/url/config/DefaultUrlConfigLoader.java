package wint.mvc.url.config;

import java.io.IOException;
import java.util.Map;

import wint.core.config.Constants;
import wint.core.io.resource.Resource;
import wint.core.io.resource.loader.ResourceLoader;
import wint.core.service.ServiceContext;
import wint.lang.exceptions.ResourceException;
import wint.lang.utils.MapUtil;

public class DefaultUrlConfigLoader implements UrlConfigLoader {
	
	private String urlConfigFile;
	
	private ResourceLoader resourceLoader;
	
	private UrlConfigParser urlConfigParser = new XmlUrlConfigParser();
	
	public DefaultUrlConfigLoader(ServiceContext serviceContext) {
		urlConfigFile = serviceContext.getConfiguration().getProperties().getString(Constants.PropertyKeys.URL_CONFIG_FILE, Constants.Defaults.URL_CONFIG_FILE);
		resourceLoader = serviceContext.getResourceLoader();
	}

	public Map<String, AbstractUrlConfig> loadUrlModules() {
		Resource resource = resourceLoader.getResource(urlConfigFile);
		if (resource == null || !resource.exist()) {
			return MapUtil.newHashMap();
		}
		try {
			return urlConfigParser.parse(resource.getInputStream());
		} catch (IOException e) {
			throw new ResourceException(e);
		}
	}

	public UrlConfigParser getUrlConfigParser() {
		return urlConfigParser;
	}

	public void setUrlConfigParser(UrlConfigParser urlConfigParser) {
		this.urlConfigParser = urlConfigParser;
	}

}
