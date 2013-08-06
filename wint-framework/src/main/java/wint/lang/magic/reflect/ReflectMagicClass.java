package wint.lang.magic.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import wint.lang.WintException;
import wint.lang.exceptions.CanNotFindMethodException;
import wint.lang.exceptions.CanNotInstanceException;
import wint.lang.magic.MagicClass;
import wint.lang.magic.MagicMethod;
import wint.lang.magic.MagicObject;
import wint.lang.magic.Property;
import wint.lang.magic.compatible.AutoAdaptMagicMethod;
import wint.lang.utils.ArrayUtil;

public class ReflectMagicClass extends MagicClass {

	private static final long serialVersionUID = 3729825268816208769L;

	public ReflectMagicClass(Class<?> targetClass) {
		super(targetClass);
	}

	@Override
	public Map<String, Property> getProperties() {
		return findPropertiesFromClass();
	}
	
	@Override
	public MagicObject newInstance() {
		return newInstance(ArrayUtil.EMPTY_CLASS_ARRAY, null);
	}

	@Override
	public MagicObject newInstance(Class<?>[] parameterTypes, Object[] constructorArguments) {
		try {
			Constructor<?> constructor = targetClass.getConstructor(parameterTypes);
			return new ReflectMagicObject(constructor.newInstance(constructorArguments));
		} catch (SecurityException e) {
			throw new CanNotInstanceException(e);
		} catch (NoSuchMethodException e) {
			throw new CanNotInstanceException(e);
		} catch (IllegalArgumentException e) {
			throw new CanNotInstanceException(e);
		} catch (InstantiationException e) {
			throw new CanNotInstanceException(e);
		} catch (IllegalAccessException e) {
			throw new CanNotInstanceException(e);
		} catch (InvocationTargetException e) {
			throw new CanNotInstanceException(e.getTargetException());
		}
	}
	
	@Override
	public MagicMethod getMethod(String methodName) {
		Method method = getMethodByName(methodName);
		if (method != null) {
			return new AutoAdaptMagicMethod(new ReflectMagicMethod(method));
		} else {
			return null;
		}
	}

	@Override
	public MagicMethod getMethod(String methodName, Class<?>[] argumentTypes) {
		try {
			Method method = targetClass.getMethod(methodName, argumentTypes);
			return new ReflectMagicMethod(method);
		} catch (SecurityException e) {
			throw new WintException(e);
		} catch (NoSuchMethodException e) {
			throw new CanNotFindMethodException(e);
		}
	}

	@Override
	public String toString() {
		return "ReflectMagicClass [" + targetClass + "]";
	}

}
