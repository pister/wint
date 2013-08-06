package wint.lang.magic.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import wint.lang.exceptions.MethodInvokeException;
import wint.lang.magic.MagicClass;
import wint.lang.magic.MagicList;
import wint.lang.magic.MagicMethod;

public class ReflectMagicMethod extends MagicMethod {

	public ReflectMagicMethod(Method method) {
		super(method);
	}

	@Override
	public Object invoke(Object object, Object[] arguments) {
		try {
			targetMethod.setAccessible(true);
			return targetMethod.invoke(object, arguments);
		} catch (IllegalArgumentException e) {
			throw e;
		} catch (IllegalAccessException e) {
			throw new MethodInvokeException(e);
		} catch (InvocationTargetException e) {
			throw new MethodInvokeException(e.getTargetException());
		}
	}
	
	public MagicList<MagicClass> getParameterTypes() {
		Class<?>[] types = targetMethod.getParameterTypes();
		MagicList<MagicClass> ret = MagicList.newList();
		for (Class<?> t : types) {
			ret.add(new ReflectMagicClass(t));
		}
		return ret;
	}

	@Override
	public String toString() {
		return "ReflectMagicMethod [" + targetMethod + "]";
	}
	
}