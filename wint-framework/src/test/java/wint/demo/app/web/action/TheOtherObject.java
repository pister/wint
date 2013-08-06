package wint.demo.app.web.action;

import java.io.Serializable;

public class TheOtherObject implements Serializable {

	private static final long serialVersionUID = 498021217096428707L;

	private String description;
	
	private int type;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
}
