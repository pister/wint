package wint.mvc.pipeline.valves;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wint.core.service.ServiceContext;
import wint.core.service.initial.Initializor;
import wint.core.service.initial.ServiceContextAwire;

/**
 * @author pister
 * 2011-12-28 06:02:52
 */
public abstract class AbstractValve implements Valve, ServiceContextAwire, Initializor {

	protected final Logger log = LoggerFactory.getLogger(getClass());
	
	protected ServiceContext serviceContext;
	
	private String label;
	
	public void init() {
		
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getName() {
		return getClass().getName();
	}

	public final void setServiceContext(ServiceContext serviceContext) {
		this.serviceContext = serviceContext;
	}

}
