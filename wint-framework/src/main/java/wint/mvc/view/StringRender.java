package wint.mvc.view;

/**
 * @author pister 2012-3-3 07:51:46
 */
public class StringRender implements Render {

	private String string;
	
	public StringRender(String string) {
		super();
		this.string = string;
	}

	public String render() {
		return string;
	}

}
