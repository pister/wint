package wint.core.config;

import wint.core.config.property.MagicPropertiesMap;
import wint.core.config.property.PropertiesMap;
import wint.core.service.definition.ServiceDefinition;
import wint.core.service.env.Environment;
import wint.lang.magic.MagicMap;

public class Configuration {

    private ServiceDefinition serviceDefinition;

    private PropertiesMap properties;

    private Environment environment;

    public ServiceDefinition getServiceDefinition() {
        return serviceDefinition;
    }

    public void setServiceDefinition(ServiceDefinition serviceDefinition) {
        this.serviceDefinition = serviceDefinition;
    }


    public PropertiesMap getProperties() {
        return properties;
    }


    public void setProperties(MagicMap appProperties) {
        this.properties = new MagicPropertiesMap(appProperties);
        this.environment = Environment.valueFromName(properties.getString(Constants.PropertyKeys.APP_ENV));
    }

    public Environment getEnvironment() {
        return environment;
    }


}
