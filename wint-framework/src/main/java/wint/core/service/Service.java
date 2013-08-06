package wint.core.service;

import wint.core.service.initial.Initializor;


public interface Service extends Initializor {
	
	void destroy();
	
	String getName();
	
	ServiceContext getServiceContext();
	

}
