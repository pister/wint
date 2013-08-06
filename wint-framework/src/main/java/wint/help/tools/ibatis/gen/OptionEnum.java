package wint.help.tools.ibatis.gen;

public enum OptionEnum {
	
	READ(1),
	WRITE(2),
	READ_AND_WRITE(3)
	;
	
	private OptionEnum(int value) {
		this.value = value;
	}
	
	private int value;

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
}
