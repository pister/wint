package wint.tools.similar.content;

public class StringContent extends Content {

	private String content;
	
	public StringContent(String id, String content) {
		this.id = id;
		this.content = content;
	}
	
	@Override
	public String getValue() {
		return content;
	}

}
