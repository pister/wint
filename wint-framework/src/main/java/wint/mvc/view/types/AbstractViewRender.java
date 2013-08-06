package wint.mvc.view.types;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wint.core.service.ServiceContext;
import wint.core.service.initial.Initializor;
import wint.core.service.initial.ServiceContextAwire;
import wint.mvc.flow.InnerFlowData;
import wint.mvc.template.Context;
import wint.mvc.view.ViewRender;

public abstract class AbstractViewRender implements ViewRender, ServiceContextAwire, Initializor {

	protected final Logger log = LoggerFactory.getLogger(getClass());
	
	protected ServiceContext serviceContext;
	
	public abstract String getViewType();

	public abstract void render(Context context, InnerFlowData flowData, String target, String moduleType);

	public final void setServiceContext(ServiceContext serviceContext) {
		this.serviceContext = serviceContext;
	}

	public void init() {
		
	}

}
