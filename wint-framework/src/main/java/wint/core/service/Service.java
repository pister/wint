package wint.core.service;

import wint.core.service.initial.Initializor;


/**
 * 服务接口
 * @author
 */
public interface Service extends Initializor {
	
	void destroy();
	
	String getName();
	
	ServiceContext getServiceContext();
	

}
