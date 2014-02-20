package wint.core.service;

import java.util.Set;

import wint.core.config.Configuration;
import wint.core.io.resource.loader.ResourceLoader;
import wint.core.service.bean.BeanFactory;
import wint.lang.magic.Property;

/**
 * 服务容器
 * @author pister
 */
public interface ServiceContext extends BeanFactory {

    /**
     * 通过类型获取对应类型的实例
     * @param clazz
     * @param <T>
     * @return
     */
	<T> T getService(Class<? extends Service> clazz);
	
	Set<Property> injectProperties(Object target);
	
	Configuration getConfiguration();
	
	ResourceLoader getResourceLoader();
	
	BeanFactory getObjectFactory();
	
}
