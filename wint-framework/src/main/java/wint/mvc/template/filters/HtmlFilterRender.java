package wint.mvc.template.filters;

import wint.lang.utils.EscapeUtil;

public class HtmlFilterRender extends AbstractFilterRender {

	public HtmlFilterRender(Object content) {
		super(content);
	}

	public String render() {
		return EscapeUtil.escapeHtmlSimple(content);
	}

}
