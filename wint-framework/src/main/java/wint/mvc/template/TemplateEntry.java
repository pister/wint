package wint.mvc.template;

import java.io.Serializable;

public class TemplateEntry implements Serializable {

	private static final long serialVersionUID = -4275763574810176770L;

	public TemplateEntry() {
		super();
	}
	public TemplateEntry(String path, String templateExtension) {
		super();
		this.path = path;
		this.templateExtension = templateExtension;
	}
	
	private String path;
	
	private String templateExtension;
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getTemplateExtension() {
		return templateExtension;
	}
	public void setTemplateExtension(String templateExtension) {
		this.templateExtension = templateExtension;
	}
	
}
