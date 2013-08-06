package wint.lang.magic;

import java.lang.reflect.Field;
import java.util.List;

import wint.lang.WintException;
import wint.lang.exceptions.CanNotFindMethodException;
import wint.lang.magic.config.MagicFactory;
import wint.lang.utils.ArrayUtil;
import wint.lang.utils.ObjectUtil;

/**
 * @author pister 2011-12-22 10:05:59
 */
public abstract class MagicObject {

	protected final Object targetObject;
	
	protected final MagicClass magicClass;
	
	public Object getField(String name) {
		Field f = magicClass.getField(name);
		try {
			return f.get(targetObject);
		} catch (Exception e) {
			throw new WintException(e);
		}
	}
	
	public Object getPropertyValue(String propertyName) {
		Property property = magicClass.getProperty(propertyName);
		if (property == null) {
			return null;
		}
		return property.getValue(targetObject);
	}

	public Object invoke(String methodName, Class<?>[] argumentTypes, Object[] arguments) {
		ensureNotNull();
		return magicClass.getMethod(methodName, argumentTypes).invoke(targetObject, arguments);
	}
	
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

	public void setPropertyValue(String propertyName, Object newValue) {
		magicClass.getProperty(propertyName).setValue(targetObject, newValue);
	}
	
	/**
	 * @param propertyName
	 * @param newValue
	 */
	public void setPropertyValueExt(String propertyName, Object newValue) {
		magicClass.getProperty(propertyName).setValueExt(targetObject, newValue);
	}

	public Object getObject() {
		return targetObject;
	}
	
	protected MagicObject(Object targetObject) {
		this.targetObject = targetObject;
		this.magicClass = MagicClass.wrap(targetObject);
	}
	
	public Object invoke(String methodName) {
		return invoke(methodName, ArrayUtil.EMPTY_CLASS_ARRAY, null);
	}
	
	public abstract MagicObject asProxyObject(List<? extends Interceptor> interceptors);
	
	
	protected void ensureNotNull() {
		if (targetObject == null) {
			throw new NullPointerException("object is null at MagicObject@" + Integer.toHexString(this.hashCode()));
		}
	}
	
	/**
	 * @param anothor
	 * @return
	 */
	public boolean equalSame(MagicObject anothor) {
		if (anothor == null) {
			return false;
		}
		return targetObject == anothor.targetObject;
	}
	
	
	/**
	 * @param anothor
	 * @return
	 */
	public boolean equalNormal(MagicObject anothor) {
		if (anothor == null) {
			return false;
		}
		return ObjectUtil.equals(targetObject,  anothor.targetObject);
	}
	
	public boolean equalLike(MagicObject anothor) {
		// TODO 
		return false;
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
	
	public Object get() {
		return targetObject;
	}
	
	public MagicClass getMagicClass() {
		return magicClass;
	}

	public static MagicObject wrap(Object object) {
		return MagicFactory.getMagicFactory().newMagicObject(object);
	}

}
