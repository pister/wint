package wint.core.service.supports;

import wint.core.config.Configuration;
import wint.core.service.Service;
import wint.core.service.ServiceContext;
import wint.core.service.bean.BeanFactory;
import wint.core.service.util.ServiceUtil;

public abstract class AbstractServiceContext extends AbstractBeanFactory implements ServiceContext {

	protected Configuration configuration;
	
	@SuppressWarnings("unchecked")
	public <T> T getService(Class<? extends Service> clazz) {
		return (T)getObject(ServiceUtil.getSerivceName(clazz));
	}
	
	public Configuration getConfiguration() {
		return configuration;
	}

	public BeanFactory getObjectFactory() {
		return this;
	}
}
