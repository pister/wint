package wint.mvc.view;

import wint.core.service.Service;
import wint.mvc.flow.InnerFlowData;
import wint.mvc.template.engine.TemplateEngine;

public interface ViewRenderService extends Service {
	
	TemplateEngine getTemplateEngine(String name);
	
	ViewRender getViewRender(InnerFlowData flowData, String viewType);
	
	ViewRender getDefaultViewRender();

}
