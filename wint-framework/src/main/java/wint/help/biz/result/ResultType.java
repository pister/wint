package wint.help.biz.result;

public enum ResultType {

	CURRENT_TARGET(1, "当前目标"),
	COMMON_TARGET(2, "通用目标");
	
	private int value;
	
	private String name;
	
	private ResultType(int value, String name) {
		this.value = value;
		this.name = name;
	}

	public int getValue() {
		return value;
	}

	public String getName() {
		return name;
	}
}
