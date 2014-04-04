package wint.lang.magic;

import java.lang.reflect.Field;
import java.util.List;

import wint.lang.WintException;
import wint.lang.exceptions.CanNotFindMethodException;
import wint.lang.magic.config.MagicFactory;
import wint.lang.utils.ArrayUtil;
import wint.lang.utils.ObjectUtil;

/**
 * 方便操作对象的包装
 * @author pister 2011-12-22 10:05:59
 */
public abstract class MagicObject {

	protected final Object targetObject;
	
	protected final MagicClass magicClass;

    /**
     * 获取字段值
     * @param name
     * @return
     */
	public Object getField(String name) {
		Field f = magicClass.getField(name);
		try {
			return f.get(targetObject);
		} catch (Exception e) {
			throw new WintException(e);
		}
	}

    /**
     * 获取属性值
     * @param propertyName
     * @return
     */
	public Object getPropertyValue(String propertyName) {
		Property property = magicClass.getProperty(propertyName);
		if (property == null) {
			return null;
		}
		return property.getValue(targetObject);
	}

    /**
     * 方法调用
     * @param methodName
     * @param argumentTypes
     * @param arguments
     * @return
     */
	public Object invoke(String methodName, Class<?>[] argumentTypes, Object[] arguments) {
		ensureNotNull();
		return magicClass.getMethod(methodName, argumentTypes).invoke(targetObject, arguments);
	}

    /**
     * 方法调用（不支持方法重载）
     * @param methodName
     * @param arguments
     * @return
     */
	public Object invoke(String methodName, Object[] arguments) {
		ensureNotNull();
		MagicMethod magicMethod = magicClass.getMethod(methodName);
		if (magicMethod == null) {
			throw new CanNotFindMethodException("can not find method: " + methodName);
		}
		return magicMethod.invoke(targetObject, arguments);
	}
	
	public boolean isArray() {
		return magicClass.isArray();
	}

    /**
     * 设置属性值
     * @param propertyName
     * @param newValue
     */
	public void setPropertyValue(String propertyName, Object newValue) {
		magicClass.getProperty(propertyName).setValue(targetObject, newValue);
	}
	
	/**
     * 设置属性值，并做自动参数类型转换
	 * @param propertyName
	 * @param newValue
	 */
	public void setPropertyValueExt(String propertyName, Object newValue) {
		magicClass.getProperty(propertyName).setValueExt(targetObject, newValue);
	}

    /**
     * 获取被封装的目标对象
     * @return
     */
	public Object getObject() {
		return targetObject;
	}
	
	protected MagicObject(Object targetObject, MagicClass magicClass) {
		this.targetObject = targetObject;
		this.magicClass = magicClass;
	}

    /**
     * 调用无参数的方法
     * @param methodName
     * @return
     */
	public Object invoke(String methodName) {
		return invoke(methodName, ArrayUtil.EMPTY_CLASS_ARRAY, null);
	}

    /**
     * 创建一个代理对象
     * @param interceptors 代理对象被应用的拦截器
     * @return
     */
	public abstract MagicObject asProxyObject(List<? extends Interceptor> interceptors);
	
	
	protected void ensureNotNull() {
		if (targetObject == null) {
			throw new NullPointerException("object is null at MagicObject@" + Integer.toHexString(this.hashCode()));
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((targetObject == null) ? 0 : targetObject.hashCode());
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
		MagicObject other = (MagicObject) obj;
		if (targetObject == null) {
			if (other.targetObject != null)
				return false;
		} else if (!targetObject.equals(other.targetObject))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return targetObject == null ? "<null>" : targetObject.toString();
	}

	public boolean isNull() {
		return (targetObject == null);
	}

    /**
     * 同getObject
     * @return
     */
	public Object get() {
		return targetObject;
	}

    /**
     * 获取相应的包装类
     * @return
     */
	public MagicClass getMagicClass() {
		return magicClass;
	}

    /**
     * 包括一个对象
     * @param object
     * @return
     */
	public static MagicObject wrap(Object object) {
		return MagicFactory.getMagicFactory().newMagicObject(object);
	}

}
