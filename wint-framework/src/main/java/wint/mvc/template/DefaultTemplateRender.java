package wint.mvc.template;

import wint.mvc.template.engine.TemplateEngine;


public class DefaultTemplateRender implements TemplateRender {

	private String path;

	private TemplateEngine viewRenderEngine;
	
	private Context context;
	
	public DefaultTemplateRender(String path, TemplateEngine viewRenderEngine, Context context) {
		super();
		this.path = path;
		this.viewRenderEngine = viewRenderEngine;
		this.context = context;
	}

	public String getPath() {
		return path;
	}
	
	public String render() {
		return viewRenderEngine.render(this, context);
	}



}
