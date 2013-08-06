package wint.lang.magic.config;

public enum MagicType {
	
	AUTO("auto"),
	JAVA("java"),
	CGLIB("cglib")
	;
	
	private MagicType(String name) {
		this.name = name;
	}

	private final String name;

	public String getName() {
		return name;
	}
	
	public static MagicType fromName(String name) {
		for (MagicType t : MagicType.values()) {
			if (t.name.equals(name)) {
				return t;
			}
		}
		return AUTO;
	}

}
