package wint.lang.magic.cglib;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastConstructor;
import wint.lang.WintException;
import wint.lang.exceptions.CanNotFindMethodException;
import wint.lang.magic.*;
import wint.lang.magic.compatible.AutoAdaptMagicMethod;
import wint.lang.magic.reflect.ReflectMagicClass;
import wint.lang.magic.reflect.ReflectMagicMethod;
import wint.lang.magic.reflect.ReflectMagicObject;
import wint.lang.utils.MapUtil;

/**
 * @author pister 2012-3-5 10:38:42
 */
public class CglibMagicClass extends MagicClass {

	private static final long serialVersionUID = -3133806326517090028L;

	private FastClass fastClass;
	
	protected CglibMagicClass(Class<?> inputClass) {
		super(CglibUtil.getJavaClass(inputClass));
		fastClass = FastClass.create(this.targetClass);
	}
	
	public static MagicClass fromClass(Class<?> inputClass) {
		if (inputClass.isPrimitive()) {
			return new ReflectMagicClass(inputClass);
		}
        if (inputClass.isArray()) {
            return new MagicArrayClass(inputClass);
        }
		return new CglibMagicClass(inputClass);
	}
	
	@Override
	public MagicMethod getMethod(String methodName) {
		Method method = getMethodByName(methodName);
		if (method != null) {
			return new AutoAdaptMagicMethod(new CglibMagicMethod(method, fastClass));
		} else {
			return null;
		}
	}

	@Override
	public MagicMethod getMethod(String methodName, Class<?>[] argumentTypes) {
		try {
			Method method = targetClass.getMethod(methodName, argumentTypes);
			return new CglibMagicMethod(method, fastClass);
		} catch (SecurityException e) {
			throw new WintException(e);
		} catch (NoSuchMethodException e) {
			throw new CanNotFindMethodException(e);
		}
	}

	@Override
	public Map<String, Property> getProperties() {
		return findPropertiesFromClass();
	}

	@Override
	public MagicObject newInstance() {
		try {
			return new CglibMagicObject(fastClass.newInstance());
		} catch (InvocationTargetException e) {
			throw new WintException(e.getTargetException());
		}
	}

	@Override
	public MagicObject newInstance(Class<?>[] parameterTypes, Object[] constructorArguments) {
		try {
			FastConstructor fastConstructor = fastClass.getConstructor(parameterTypes);
			return new CglibMagicObject(fastConstructor.newInstance(constructorArguments));
		} catch (InvocationTargetException e) {
			throw new WintException(e.getTargetException());
		} catch (Exception e) {
			throw new WintException(e);
		}
	}
	
	protected Map<String, Property> findPropertiesFromClass() {
		Method[] methods = this.targetClass.getMethods();
		Map<String, Method> readableMethods = MapUtil.newHashMap();
		Map<String, Method> writableMethods = MapUtil.newHashMap();
		for (Method method : methods) {
			if (PropertyUtil.isReadableMethod(method)) {
				String propertyName = PropertyUtil.getPropertyName(method.getName());
				readableMethods.put(propertyName, method);
			} else if (PropertyUtil.isWritableMethod(method)) {
				String propertyName = PropertyUtil.getPropertyName(method.getName());
				writableMethods.put(propertyName, method);
			}
		}
		
		Map<String, Property> ret = MapUtil.newHashMap();
		for (Map.Entry<String, Method> entry : readableMethods.entrySet()) {
			String name = entry.getKey();
			Method readMethod = entry.getValue();
			Method writeMethod = writableMethods.remove(name);
			Class<?> type = readMethod.getReturnType();
            /*
			if (type.isArray()) {
			    continue;
			}
			*/
            MagicClass propertyClass = CglibMagicClass.fromClass(type);
			Property property = new Property(name, propertyClass, new CglibMagicMethod(readMethod, fastClass), (writeMethod != null ? new CglibMagicMethod(writeMethod, fastClass) : null));
			ret.put(name, property);
		}
		for (Map.Entry<String, Method> entry : writableMethods.entrySet()) {
			String name = entry.getKey();
			Method writeMethod = entry.getValue();
			Class<?> type = writeMethod.getParameterTypes()[0];
            /*
			if (type.isArray()) {
			    continue;
			}
			*/
            MagicClass propertyClass = CglibMagicClass.fromClass(type);
			Property property = new Property(name, propertyClass, null, new CglibMagicMethod(writeMethod, fastClass));
			ret.put(name, property);
		}
		return ret;
	}

    public FastClass getFastClass() {
        return fastClass;
    }
}
