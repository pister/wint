package wint.lang.magic.compatible;

import wint.lang.magic.compatible.supports.DefaultAutoConvertManager;

public abstract class AutoConvertManager {
	
	public abstract Object convert(Object input, Class<?> targetType);
	
	private static AutoConvertManager autoConvertManager = new DefaultAutoConvertManager();
	
	public static AutoConvertManager getInstance() {
		return autoConvertManager;
	}

}
