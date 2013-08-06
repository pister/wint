package wint.mvc.template.filters;

import wint.lang.utils.EscapeUtil;

public class XmlFilterRender extends AbstractFilterRender {

	public XmlFilterRender(Object content) {
		super(content);
	}

	@Override
	public String render() {
		return EscapeUtil.escapeXml(content);
	}

}
