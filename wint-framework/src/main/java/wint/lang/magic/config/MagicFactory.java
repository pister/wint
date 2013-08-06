package wint.lang.magic.config;

import wint.lang.magic.MagicClass;
import wint.lang.magic.MagicObject;

public abstract class MagicFactory {
	
	protected MagicFactory() {
		super();
	}

	public abstract MagicClass newMagicClass(Class<?> clazz);

	public abstract MagicObject newMagicObject(Object object);
	
	public abstract String getName();
	
	public static MagicFactory getMagicFactory() {
		return MagicConfig.getMagicConfig().getMagicFactory();
	}
	
}
