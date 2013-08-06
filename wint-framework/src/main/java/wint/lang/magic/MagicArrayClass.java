package wint.lang.magic;

import java.lang.reflect.Array;
import java.util.Map;

import wint.lang.WintException;

public class MagicArrayClass extends MagicClass {

	private static final long serialVersionUID = 39915621008081990L;

	protected Class<?> componentType;
	
	protected MagicArrayClass(Class<?> targetClass) {
		super(targetClass);
		this.componentType = targetClass.getComponentType();
	}

	@Override
	public Map<String, Property> getProperties() {
		throw new WintException("there was no properties in array:" + componentType);
	}

	@Override
	public MagicObject newInstance() {
		throw new WintException("can not instance a array :" + componentType);
	}

	@Override
	public MagicObject newInstance(Class<?>[] parameterTypes, Object[] constructorArguments) {
		throw new WintException("can not instance a array :" + componentType);
	}

	@Override
	public MagicMethod getMethod(String methodName) {
		throw new WintException("there was not method in array");
	}

	@Override
	public MagicMethod getMethod(String methodName, Class<?>[] argumentTypes) {
		throw new WintException("there was not method in array");
	}
	
	public MagicClass getComponentType() {
		return MagicClass.wrap(componentType);
	}

	public MagicObject newInstance(int length) {
		Object array = Array.newInstance(componentType, length);
		return new MagicArrayObject(array);
	}
}
