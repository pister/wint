package wint.core.service.env;

public enum Environment {
	
	DEV("dev"),
	TEST("test"),
	PRODUCT("product");
	
	private Environment(String name) {
		this.name = name;
	}
	
	private final String name;

	public String getName() {
		return name;
	}
	
	public static Environment valueFromName(String name) {
		for (Environment e : values()) {
			if (e.name.equals(name)) {
				return e;
			}
		}
		return Environment.DEV;
	}
	

}
