package wint.mvc.template;

import wint.core.service.Service;

public interface LoadTemplateService extends Service {
	
	TemplateRender loadTemplate(String templateName, Context context, String type);

}
