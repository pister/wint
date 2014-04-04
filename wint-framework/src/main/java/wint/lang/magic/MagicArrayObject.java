package wint.lang.magic;

import java.lang.reflect.Array;
import java.util.List;

import wint.lang.WintException;

public class MagicArrayObject extends MagicObject {

	protected MagicArrayObject(Object targetObject) {
		super(targetObject, new MagicArrayClass(targetObject.getClass()));
	}

	@Override
	public MagicObject asProxyObject(List<? extends Interceptor> interceptors) {
		throw new WintException("can not proxy a array :" + targetObject);
	}

	@Override
	public boolean isArray() {
		return true;
	}

	@Override
	public Object getPropertyValue(String propertyName) {
		throw new WintException("unsupport operate on a array :" + targetObject);
	}

	@Override
	public Object invoke(String methodName, Class<?>[] argumentTypes, Object[] arguments) {
		throw new WintException("unsupport operate on a array :" + targetObject);
	}

	@Override
	public Object invoke(String methodName, Object[] arguments) {
		throw new WintException("unsupport operate on a array :" + targetObject);
	}

	@Override
	public void setPropertyValue(String propertyName, Object newValue) {
		throw new WintException("unsupport operate on a array :" + targetObject);
	}

	@Override
	public void setPropertyValueExt(String propertyName, Object newValue) {
		throw new WintException("unsupport operate on a array :" + targetObject);
	}

	@Override
	public Object invoke(String methodName) {
		throw new WintException("unsupport operate on a array :" + targetObject);
	}

	public int getLength() {
		return Array.getLength(targetObject);
	}
	
	public Object get(int index) {
		return Array.get(targetObject, index);
	}
	
	public void set(int index, Object value) {
		Array.set(targetObject, index, value);
	}
}
