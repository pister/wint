package wint.lang.magic.config;

import wint.lang.magic.MagicClass;
import wint.lang.magic.MagicObject;
import wint.lang.magic.reflect.ReflectMagicClass;
import wint.lang.magic.reflect.ReflectMagicObject;

public class ReflectMagicFactory extends MagicFactory {

	public ReflectMagicFactory() {
	}

	public MagicClass newMagicClass(Class<?> clazz) {
		return new ReflectMagicClass(clazz);
	}

	public MagicObject newMagicObject(Object object) {
		return new ReflectMagicObject(object);
	}

	@Override
	public String getName() {
		return "java";
	}

}
