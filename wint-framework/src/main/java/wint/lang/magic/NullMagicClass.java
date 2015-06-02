package wint.lang.magic;

import java.util.Map;

import wint.lang.utils.MapUtil;

/**
 * @author pister
 */
public class NullMagicClass extends MagicClass {

	private static final long serialVersionUID = -4272003387109471285L;

	protected NullMagicClass() {
		super(null);
	}
	
	private static NullMagicClass instance = new NullMagicClass();
	
	public static NullMagicClass getInstance() {
		return instance;
	}

	static class NothingMagicMethod extends MagicMethod {

		public NothingMagicMethod() {
			super(null);
		}

		@Override
		public Object invoke(Object object, Object[] arguments) {
			return null;
		}

		@Override
		public MagicList<MagicClass> getParameterTypes() {
			return null;
		}
		
	}

	@Override
	public MagicMethod getMethod(String methodName) {
		return new NothingMagicMethod();
	}

	@Override
	public MagicMethod getMethod(String methodName, Class<?>[] argumentTypes) {
		return new NothingMagicMethod();
	}

	@Override
	public Map<String, Property> getProperties() {
		return MapUtil.newUnmodifiableMap();
	}

	@Override
	public Class<?> getTargetClass() {
		return null;
	}

	@Override
	public boolean isArray() {
		return false;
	}

	@Override
	public boolean isAssignableTo(Class<?> cls) {
		return false;
	}

	@Override
	public boolean isInstanceOf(Object object) {
		if (object == null) {
			return true;
		}
		return false;
 	}

	@Override
	public MagicObject newInstance() {
		return null;
	}

	@Override
	public MagicObject newInstance(Class<?>[] parameterTypes, Object[] constructorArguments) {
		return null;
	}

	@Override
	public boolean isPrimary() {
		return false;
	}

	@Override
	public String toString() {
		return "NullMagicClass";
	}


}
