package wint.lang.magic;

import java.lang.reflect.Method;


public abstract class MagicMethod {
	
	protected final Method targetMethod;
	
	public MagicMethod(Method targetMethod) {
		super();
		this.targetMethod = targetMethod;
	}

	public abstract Object invoke(Object object, Object[] arguments);
	
	public Object invoke(MagicObject magicObject, Object[] arguments) {
		return invoke(magicObject.getObject(), arguments);
	}
	
	public abstract MagicList<MagicClass> getParameterTypes();

	public Method getTargetMethod() {
		return targetMethod;
	}
	
	public String getName() {
		return targetMethod.getName();
	}
	
	public boolean isGetter() {
		Class<?>[] types = targetMethod.getParameterTypes();
		if (types != null && types.length > 0) {
			return false;
		}
		String name = getName();
		return name.startsWith("get") || name.startsWith("is");
	}
	
	
}
