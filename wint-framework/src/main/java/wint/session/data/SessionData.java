package wint.session.data;

import java.io.Serializable;

public class SessionData implements Serializable {

	private static final long serialVersionUID = -4338108268091573847L;
	
	private String name;
	
	/**
	 * 数据
	 */
	private Object data;
	
	public SessionData() {
		super();
	}

	public SessionData(String name, Object data) {
		super();
		this.name = name;
		this.data = data;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}


}
