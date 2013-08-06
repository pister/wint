package wint.core.service;

import java.util.Set;

import wint.core.config.Configuration;
import wint.core.io.resource.loader.ResourceLoader;
import wint.core.service.bean.BeanFactory;
import wint.lang.magic.Property;

public interface ServiceContext extends BeanFactory {

	<T> T getService(Class<? extends Service> clazz);
	
	/**
	 * @param target
	 * @return
	 */
	Set<Property> injectProperties(Object target);
	
	Configuration getConfiguration();
	
	ResourceLoader getResourceLoader();
	
	BeanFactory getObjectFactory();
	
}
