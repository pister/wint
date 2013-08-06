package wint.mvc.template.filters;

import wint.lang.utils.EscapeUtil;

public class JavaScriptFilterRender extends AbstractFilterRender {

	public JavaScriptFilterRender(Object content) {
		super(content);
	}

	@Override
	public String render() {
		return EscapeUtil.escapeJavaScript(content);
	}

}
