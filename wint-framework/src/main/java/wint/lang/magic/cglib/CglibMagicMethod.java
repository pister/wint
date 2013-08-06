package wint.lang.magic.cglib;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;
import wint.lang.exceptions.MethodInvokeException;
import wint.lang.magic.MagicClass;
import wint.lang.magic.MagicList;
import wint.lang.magic.MagicMethod;

/**
 * @author pister 2012-3-5 10:38:39
 */
public class CglibMagicMethod extends MagicMethod {

	private FastMethod fastMethod;
	
	public CglibMagicMethod(Method targetMethod, FastClass fastClass) {
		super(targetMethod);
		fastMethod = fastClass.getMethod(targetMethod);
	}
	public MagicList<MagicClass> getParameterTypes() {
		Class<?>[] types = targetMethod.getParameterTypes();
		MagicList<MagicClass> ret = MagicList.newList();
		for (Class<?> t : types) {
			ret.add(CglibMagicClass.fromClass(t));
		}
		return ret;
	}
	
	@Override
	public Object invoke(Object object, Object[] arguments) {
		try {
			return fastMethod.invoke(object, arguments);
		} catch (InvocationTargetException e) {
			throw new MethodInvokeException(e.getTargetException());
		}
	}

}
