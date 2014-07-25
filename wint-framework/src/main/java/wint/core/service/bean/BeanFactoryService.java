package wint.core.service.bean;

import wint.core.service.Service;

/**
 * @author pister 2012-3-4 03:48:07
 */
public interface BeanFactoryService extends Service {
	
	BeanFactory getBeanFactory();

    Object getApplicationContext();
}
