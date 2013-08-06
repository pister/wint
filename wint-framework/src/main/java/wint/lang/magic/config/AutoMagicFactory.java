package wint.lang.magic.config;

import wint.lang.magic.MagicClass;
import wint.lang.magic.MagicObject;
import wint.lang.utils.LibUtil;

public class AutoMagicFactory extends MagicFactory {
	
	private MagicFactory magicFactory;

	public AutoMagicFactory() {
		if (LibUtil.isCglibExist()) {
			try {
				magicFactory = (MagicFactory)Class.forName("wint.lang.magic.config.CglibMagicFactory").newInstance();
			} catch (Exception e) {
				magicFactory = new ReflectMagicFactory();
			}
		} else {
			magicFactory = new ReflectMagicFactory();
		}
	}
	
	@Override
	public String getName() {
		return magicFactory.getName();
	}

	public MagicClass newMagicClass(Class<?> clazz) {
		return magicFactory.newMagicClass(clazz);
	}

	public MagicObject newMagicObject(Object object) {
		return magicFactory.newMagicObject(object);
	}

}
