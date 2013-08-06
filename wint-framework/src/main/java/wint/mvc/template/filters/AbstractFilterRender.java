package wint.mvc.template.filters;

import wint.mvc.view.Render;

public abstract class AbstractFilterRender implements Render {

	protected String content;
	
	public AbstractFilterRender(Object content) {
		super();
		this.content = content == null ? null : content.toString();
	}

	public abstract String render();

}
