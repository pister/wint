package wint.lang.magic.config;

import wint.lang.magic.MagicClass;
import wint.lang.magic.MagicObject;
import wint.lang.magic.cglib.CglibMagicClass;
import wint.lang.magic.cglib.CglibMagicObject;

public class CglibMagicFactory extends MagicFactory {

	public CglibMagicFactory() {
	}

	public MagicClass newMagicClass(Class<?> clazz) {
		return CglibMagicClass.fromClass(clazz);
	}

	public MagicObject newMagicObject(Object object) {
		return new CglibMagicObject(object);
	}

	@Override
	public String getName() {
		return "cglib";
	}

}
