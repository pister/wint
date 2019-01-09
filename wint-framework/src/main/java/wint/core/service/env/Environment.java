package wint.core.service.env;

public enum Environment {
	
	MOCK("mock", true),
    DEV("dev", true),
	TEST("test", false),
	PREPUB("prepub", false),
	PRODUCT("product", false);

	private Environment(String name, boolean supportDev) {
		this.name = name;
        this.supportDev = supportDev;
	}
	
	private final String name;

    private final boolean supportDev;

	public String getName() {
		return name;
	}

    public boolean isSupportDev() {
        return supportDev;
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
