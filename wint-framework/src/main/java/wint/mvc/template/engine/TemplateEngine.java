package wint.mvc.template.engine;

import wint.core.service.ServiceContext;
import wint.mvc.template.Context;
import wint.mvc.template.TemplateRender;

import java.io.File;

public interface TemplateEngine {
	
	String render(TemplateRender templateResource, Context context);
	
	String getName();
	
	void init(ServiceContext serviceContext);

    void setBasePath(String baseFile);

}
