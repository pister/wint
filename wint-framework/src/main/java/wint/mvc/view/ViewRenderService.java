package wint.mvc.view;

import wint.core.service.Service;
import wint.mvc.template.engine.TemplateEngine;

public interface ViewRenderService extends Service {
	
	TemplateEngine getTemplateEngine(String name);
	
	ViewRender getViewRender(String viewType);
	
	ViewRender getDefaultViewRender();

}
