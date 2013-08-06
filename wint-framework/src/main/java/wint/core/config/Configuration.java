package wint.core.config;

import wint.core.service.definition.ServiceDefinition;
import wint.core.service.env.Environment;
import wint.lang.magic.MagicMap;

public class Configuration {

	private ServiceDefinition serviceDefinition;
	
	private MagicMap properties = MagicMap.newMagicMap();

	private Environment environment;
	
	public ServiceDefinition getServiceDefinition() {
		return serviceDefinition;
	}

	public void setServiceDefinition(ServiceDefinition serviceDefinition) {
		this.serviceDefinition = serviceDefinition;
	}
	
	public MagicMap getProperties() {
		return properties;
	}
	

	public void setProperties(MagicMap properties) {
		this.properties = properties;
		if (properties != null) {
			environment = Environment.valueFromName(properties.getString(Constants.PropertyKeys.APP_ENV));
		}
	}

	public Environment getEnvironment() {
		return environment;
	}
	
}
