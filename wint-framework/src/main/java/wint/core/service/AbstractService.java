package wint.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wint.core.service.initial.ServiceContextAwire;
import wint.core.service.util.ServiceUtil;

/**
 * @author pister 2012-1-2 03:17:05
 */
public abstract class AbstractService implements Service, ServiceContextAwire {

	protected final Logger log = LoggerFactory.getLogger(getClass());
	
	protected ServiceContext serviceContext;
	
	protected String serviceName;
	
	protected AbstractService() {
		this.serviceName = ServiceUtil.getSerivceName(getClass());
	}
	
	public void destroy() {
		
	}

	public String getName() {
		return serviceName;
	}

	public ServiceContext getServiceContext() {
		return serviceContext;
	}

	public void init() {
	}

	public final void setServiceContext(ServiceContext serviceContext) {
		this.serviceContext = serviceContext;
	}

}
