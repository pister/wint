package wint.mvc.template.filters;

import wint.lang.utils.EscapeUtil;

public class CvsFilterRender extends AbstractFilterRender {

	public CvsFilterRender(Object content) {
		super(content);
	}

	@Override
	public String render() {
		return EscapeUtil.escapeCsv(content);
	}

}
