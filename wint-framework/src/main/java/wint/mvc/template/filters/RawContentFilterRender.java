package wint.mvc.template.filters;

public class RawContentFilterRender extends AbstractFilterRender {

	public RawContentFilterRender(Object content) {
		super(content);
	}

	@Override
	public String render() {
		return content;
	}

}
