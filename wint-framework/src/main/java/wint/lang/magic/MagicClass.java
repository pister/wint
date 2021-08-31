package wint.lang.magic;

import wint.lang.WintException;
import wint.lang.exceptions.TooManyMethodMatchesException;
import wint.lang.magic.config.MagicFactory;
import wint.lang.magic.reflect.ReflectMagicClass;
import wint.lang.magic.reflect.ReflectMagicMethod;
import wint.lang.utils.ClassUtil;
import wint.lang.utils.MapUtil;
import wint.lang.utils.SystemUtil;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * class魔术包装类，提供了简单方便的class操作，
 * 实现上根据环境分为反射实现和cglib实现。 <br />
 * <p/>
 * 使用可以用 MagicClass.wrap(Class) 或是 Magic.forName(ClassName)来包括
 * <p/>
 * 获取原始的可以通过 MagicClass.getTargetClass()方法获取
 *
 * @author pister 2011-12-22 10:07:21
 */
public abstract class MagicClass implements Serializable {

    private static final long serialVersionUID = 2736682505286158072L;
    protected Class<?> targetClass;

    protected MagicClass(Class<?> targetClass) {
        super();
        this.targetClass = targetClass;
    }

    /**
     * 根据类名包装
     *
     * @param className
     * @return
     */
    public static MagicClass forName(String className) {
        return wrap(ClassUtil.forName(className));
    }

    /**
     * 根据类包装
     *
     * @param clazz
     * @return
     */
    public static MagicClass wrap(Class<?> clazz) {
        return MagicFactory.getMagicFactory().newMagicClass(clazz);
    }

    /**
     * 根据对象包装
     *
     * @param object
     * @return
     */
    public static MagicClass wrap(Object object) {
        if (object == null) {
            return NullMagicClass.getInstance();
        }
        return wrap(object.getClass());
    }

    /**
     * 获取所有属性
     *
     * @return
     */
    public abstract Map<String, Property> getProperties();

    /**
     * 获取所有可读属性
     *
     * @return
     */
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

    /**
     * 获取所有可读and可写属性
     *
     * @return
     */
    public Map<String, Property> getReadAndWritableProperties() {
        Map<String, Property> ret = MapUtil.newHashMap();
        Map<String, Property> propertyMap = getProperties();
        for (Map.Entry<String, Property> entry : propertyMap.entrySet()) {
            if (entry.getValue().isReadable() && entry.getValue().isWritable()) {
                ret.put(entry.getKey(), entry.getValue());
            }
        }
        return ret;
    }

    /**
     * 获取所有可写属性
     *
     * @return
     */
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

    /**
     * 根据名称获取属性
     *
     * @param name
     * @return
     */
    public Property getProperty(String name) {
        return getProperties().get(name);
    }

    /**
     * 根据名称获取字段（字段可能为private）
     *
     * @param name
     * @return
     */
    public Field getField(final String name) {
        return (Field) AccessController.doPrivileged(new PrivilegedAction<Object>() {
            @Override
            public Object run() {
                try {
                    Field f = targetClass.getDeclaredField(name);
                    f.setAccessible(true);
                    return f;
                } catch (NoSuchFieldException e) {
                    throw new WintException(e);
                }
            }
        });
    }

    /**
     * 根据默认构造函数，创建实例
     *
     * @return
     */
    public abstract MagicObject newInstance();

    /**
     * 根据特定的构造函数，创建实例
     *
     * @param parameterTypes       构造函数类型
     * @param constructorArguments 构造函数参数
     * @return
     */
    public abstract MagicObject newInstance(Class<?>[] parameterTypes, Object[] constructorArguments);

    /**
     * 获取被包装的目标class
     *
     * @return
     */
    public Class<?> getTargetClass() {
        return targetClass;
    }

    /**
     * 是否是数组
     *
     * @return
     */
    public boolean isArray() {
        return targetClass.isArray();
    }

    /**
     * 是否枚举
     *
     * @return
     */
    public boolean isEnum() {
        return targetClass.isEnum();
    }

    /**
     * 是否是collection或是array
     *
     * @return
     */
    public boolean isCollectionLike() {
        if (isArray()) {
            return true;
        }
        return (Collection.class.isAssignableFrom(targetClass));
    }

    /**
     * 是否是map
     *
     * @return
     */
    public boolean isMap() {
        return (Map.class.isAssignableFrom(targetClass));
    }

    /**
     * 根据名称获取方法，如果有多种重载，则抛出异常，如果没有，返回null
     *
     * @param methodName
     * @return
     */
    public abstract MagicMethod getMethod(String methodName);

    /**
     * 返回该方法名的方法，返回所有重载方法
     * @param methodName
     * @return
     */
    public abstract List<MagicMethod> getMethods(String methodName);

    /**
     * 根据名称，方法参数获取方法，如果没有，返回null
     *
     * @param methodName
     * @param argumentTypes
     * @return
     */
    public abstract MagicMethod getMethod(String methodName, Class<?>[] argumentTypes);

    /**
     * 参数中的对象是否是该类的实例
     *
     * @param object
     * @return
     */
    public boolean isInstanceOf(Object object) {
        return targetClass.isInstance(object);
    }

    /**
     * @param object
     * @return
     * @deprecated instead of isInstanceOf
     */
    public boolean isInstanceof(Object object) {
        return targetClass.isInstance(object);
    }

    /**
     * 参数中的类型是否能赋值给该类
     *
     * @param cls
     * @return
     */
    public boolean isAssignableTo(Class<?> cls) {
        return cls.isAssignableFrom(targetClass);
    }

    /**
     * 是否基础类型
     *
     * @return
     */
    public boolean isPrimary() {
        return targetClass.isPrimitive();
    }

    /**
     * 是否为数字类型
     *
     * @return
     */
    public boolean isNumeric() {
        if (isAssignableTo(Number.class)) {
            return true;
        }
        return ClassUtil.isPrimaryNumericClass(targetClass);
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
            throw new TooManyMethodMatchesException("too many methods find by name:" + methodName + " are" +
                    SystemUtil.LINE_SEPARATOR +
                    ret.join(SystemUtil.LINE_SEPARATOR));
        }
        return ret.get(0);
    }

    protected List<Method> getMethodsByName(String methodName) {
        MagicList<Method> ret = MagicList.newList();
        for (Method method : targetClass.getMethods()) {
            if (method.getName().equals(methodName)) {
                ret.add(method);
            }
        }
        return ret;
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
        Map<String, Property> ret = new LinkedHashMap<String, Property>();
        for (Map.Entry<String, Method> entry : readableMethods.entrySet()) {
            String name = entry.getKey();
            Method readMethod = entry.getValue();
            Method writeMethod = writableMethods.remove(name);
            MagicClass propertyClass = new ReflectMagicClass(readMethod.getReturnType());
            Property property = new Property(name, propertyClass, new ReflectMagicMethod(readMethod), (writeMethod != null ? new ReflectMagicMethod(writeMethod) : null));
            ret.put(name, property);
        }
        for (Map.Entry<String, Method> entry : writableMethods.entrySet()) {
            String name = entry.getKey();
            Method writeMethod = entry.getValue();
            MagicClass propertyClass = new ReflectMagicClass(writeMethod.getParameterTypes()[0]);
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
