package wint.lang.magic;

import wint.lang.exceptions.PropertyAccessException;
import wint.lang.magic.compatible.AutoAdaptMagicMethod;
import wint.lang.utils.ArrayUtil;

/**
 * @author pister 2011-12-22 10:05:55
 */
public class Property {

	private String name;

	private MagicClass propertyClass;
	
	private MagicMethod readMethod;
	
	private MagicMethod writeMethod;

	public Property() {
		super();
	}

	public Property(String name, MagicClass propertyClass, MagicMethod readMethod, MagicMethod writeMethod) {
		super();
		this.name = name;
		this.propertyClass = propertyClass;
		this.readMethod = readMethod;
		this.writeMethod = writeMethod;
	}

	public boolean isReadable() {
		return readMethod != null;
	}

	public boolean isWritable() {
		return writeMethod != null;
	}

	public String getName() {
		return name;
	}

	public MagicClass getPropertyClass() {
		return propertyClass;
	}
	
	public Object getValue(Object owner) {
		if (!isReadable()) {
			throw new PropertyAccessException("can not read the property: " + name);
		}
		return readMethod.invoke(owner, ArrayUtil.EMPTY_OBJECT_ARRAY);
	}
	
	public void setValue(Object owner, Object newValue) {
		if (!isWritable()) {
			throw new PropertyAccessException("can not write the property: " + name);
		}
		writeMethod.invoke(owner, new Object[] { newValue });
	}
	
	/**
	 * @param owner
	 * @param newValue
	 */
	public void setValueExt(Object owner, Object newValue) {
		if (!isWritable()) {
			throw new PropertyAccessException("can not write the property: " + name);
		}
		AutoAdaptMagicMethod autoAdaptReflectMagicMethod = new AutoAdaptMagicMethod(writeMethod);
		autoAdaptReflectMagicMethod.invoke(owner, new Object[] { newValue });
	}

	@Override
	public String toString() {
		return "Property [name=" + name + ", propertyClass=" + propertyClass + "]";
	}


}
