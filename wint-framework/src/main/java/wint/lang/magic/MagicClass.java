package wint.lang.magic;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

import wint.lang.WintException;
import wint.lang.exceptions.TooManyMethodMatchesException;
import wint.lang.magic.config.MagicFactory;
import wint.lang.magic.reflect.ReflectMagicMethod;
import wint.lang.utils.ClassUtil;
import wint.lang.utils.MapUtil;
import wint.lang.utils.SystemUtil;


/**
 * @author pister 2011-12-22 10:07:21
 */
public abstract class MagicClass implements Serializable {

	private static final long serialVersionUID = 2736682505286158072L;
	
	protected Class<?> targetClass;

	protected MagicClass(Class<?> targetClass) {
		super();
		this.targetClass = targetClass;
	}

	public abstract Map<String, Property> getProperties();

    public Map<String, Property> getReadableProperties() {
        Map<String, Property> ret = MapUtil.newHashMap();
        Map<String, Property> propertyMap = getProperties();
        for (Map.Entry<String, Property> entry : propertyMap.entrySet()) {
            if (entry.getValue().isReadable()) {
                ret.put(entry.getKey(), entry.getValue());
            }
        }
        return ret;
    }

    public Map<String, Property> getWritableProperties() {
        Map<String, Property> ret = MapUtil.newHashMap();
        Map<String, Property> propertyMap = getProperties();
        for (Map.Entry<String, Property> entry : propertyMap.entrySet()) {
            if (entry.getValue().isWritable()) {
                ret.put(entry.getKey(), entry.getValue());
            }
        }
        return ret;
    }
	
	public Property getProperty(String name) {
		return getProperties().get(name);
	}
	
	public Field getField(String name) {
		try {
			Field f = targetClass.getDeclaredField(name);
			f.setAccessible(true);
			return f;
		} catch (Exception e) {
			throw new WintException(e);
		}
	}

	public abstract MagicObject newInstance();

	public abstract MagicObject newInstance(Class<?>[] parameterTypes, Object[] constructorArguments);
	
	public Class<?> getTargetClass() {
		return targetClass;
	}
	
	public boolean isArray() {
		return targetClass.isArray();
	}

    public boolean isEnum() {
        return targetClass.isEnum();
    }
	
	public boolean isCollectionLike() {
		if (isArray()) {
			return true;
		}
		return (Collection.class.isAssignableFrom(targetClass));
	}
	
	public boolean isMap() {
		return (Map.class.isAssignableFrom(targetClass));
	}

	public abstract MagicMethod getMethod(String methodName);
	
	public abstract MagicMethod getMethod(String methodName, Class<?>[] argumentTypes);
	
	public boolean isInstanceof(Object object) {
		return targetClass.isInstance(object);
	}

	public boolean isAssignableFrom(Class<?> cls) {
		return cls.isAssignableFrom(targetClass);
	}

	/**
	 * @param cls
	 * @return
	 */
	public boolean isAssignableFromCompatible(Class<?> cls) {
		if (isAssignableFrom(cls)) {
			return true;
		}
		return false;
	}
	
	public boolean isPrimary() {
		return targetClass.isPrimitive();
	}
	
	public static MagicClass forName(String className) {
		return wrap(ClassUtil.forName(className));
	}
	
	public static MagicClass wrap(Class<?> clazz) {
		return MagicFactory.getMagicFactory().newMagicClass(clazz);
	}
	
	public static MagicClass wrap(Object object) {
		if (object == null) {
			return NullMagicClass.getInstance();
		}
		return wrap(object.getClass());
	}
	
	protected Method getMethodByName(String methodName) {
		MagicList<Method> ret = MagicList.newList();
		for (Method method : targetClass.getMethods()) {
			if (method.getName().equals(methodName)) {
				ret.add(method);
			}
		}
		if (ret.isEmpty()) {
			return null;
		}
		if (ret.size() > 1) {
			throw new TooManyMethodMatchesException("too many methods find by name:" + methodName +" are"+
					SystemUtil.LINE_SEPARATOR + 
					ret.join(SystemUtil.LINE_SEPARATOR));
		}
		return ret.get(0);
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
			MagicClass propertyClass = MagicClass.wrap(readMethod.getReturnType());
			Property property = new Property(name, propertyClass, new ReflectMagicMethod(readMethod), (writeMethod != null ? new ReflectMagicMethod(writeMethod) : null));
			ret.put(name, property);
		}
		for (Map.Entry<String, Method> entry : writableMethods.entrySet()) {
			String name = entry.getKey();
			Method writeMethod = entry.getValue();
			MagicClass propertyClass = MagicClass.wrap(writeMethod.getParameterTypes()[0]);
			Property property = new Property(name, propertyClass, null, new ReflectMagicMethod(writeMethod));
			ret.put(name, property);
		}
		return ret;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((targetClass == null) ? 0 : targetClass.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MagicClass other = (MagicClass) obj;
		if (targetClass == null) {
			if (other.targetClass != null)
				return false;
		} else if (!targetClass.equals(other.targetClass))
			return false;
		return true;
	}

	
}
