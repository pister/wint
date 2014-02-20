package wint.mvc.init;

import javax.servlet.ServletContext;

import wint.core.config.Configuration;
import wint.core.config.Constants;
import wint.core.io.resource.Resource;
import wint.core.io.resource.loader.DefaultResourceLoader;
import wint.core.io.resource.loader.ResourceLoader;
import wint.core.service.definition.ServiceDefinition;
import wint.core.service.parser.ServiceDefinitionParser;
import wint.core.service.parser.XmlServiceDefinitionParser;
import wint.lang.magic.MagicMap;


/**
 * 派遣初始化类
 * @author pister
 * 2012-1-11 02:37:37
 */
public class DispatcherInitializor {
	
	private ResourceLoader resourceLoader;
	
	private ResourceLoader defaultResourceLoader = new DefaultResourceLoader();
	
	private ServiceDefinitionParser serviceDefinitionParser;
	
	private InitializeLogger logger;
	
	private MagicMap properties;
	
	private ServletContext servletContext;
	
	public DispatcherInitializor(ResourceLoader resourceLoader, ServiceDefinitionParser serviceDefinitionParser,
			MagicMap appProperties, InitializeLogger logger, ServletContext servletContext) {
		super();
		this.resourceLoader = resourceLoader;
		this.serviceDefinitionParser = serviceDefinitionParser;
		this.properties = appProperties;
		this.logger = logger;
		this.servletContext = servletContext;
	}
	
	public DispatcherInitializor(ResourceLoader resourceLoader, MagicMap appProperties, InitializeLogger logger, ServletContext servletContext) {
		this(resourceLoader, new XmlServiceDefinitionParser(), appProperties, logger, servletContext);
	}

	public Configuration loadConfiguration() {
		String userDefineWintFile = properties.getString(Constants.PropertyKeys.USER_DEFINE_WINT_FILE, Constants.Defaults.USER_DEFINE_WINT_FILE);
		
		Resource userDefineResource = resourceLoader.getResource(userDefineWintFile);
		Resource systemResource = defaultResourceLoader.getResource(Constants.Defaults.SYS_WINT_FILE);
		
		ServiceDefinition serviceDefinition = serviceDefinitionParser.parse(systemResource);
		if (userDefineResource.exist()) {
			ServiceDefinition userDefineServiceDefinition = serviceDefinitionParser.parse(userDefineResource);
			serviceDefinition.overWriteServicesFrom(userDefineServiceDefinition);
		}
		Configuration configuration = new Configuration();
		configuration.setProperties(properties);
		configuration.setServiceDefinition(serviceDefinition);
		return configuration;
	}
	
	public InitializeLogger getLogger() {
		return logger;
	}
	
	public ResourceLoader getResourceLoader() {
		if (resourceLoader != null) {
			return resourceLoader;
		}
		return defaultResourceLoader;
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

}
