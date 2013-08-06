package wint.lang.magic.compatible;

import wint.lang.exceptions.CanNotFindMethodException;
import wint.lang.magic.MagicClass;
import wint.lang.magic.MagicList;
import wint.lang.magic.MagicMethod;
import wint.lang.utils.ArrayUtil;

public class AutoAdaptMagicMethod extends MagicMethod {

	private MagicMethod targetMagicMethod;
	
	private AutoConvertManager autoConvertManager;
	
	public AutoAdaptMagicMethod(MagicMethod targetMagicMethod) {
		this(targetMagicMethod, AutoConvertManager.getInstance());
	}
	
	public AutoAdaptMagicMethod(MagicMethod targetMagicMethod, AutoConvertManager autoConvertManager) {
		super(targetMagicMethod.getTargetMethod());
		this.targetMagicMethod = targetMagicMethod;
		this.autoConvertManager = autoConvertManager;
	}

	@Override
	public Object invoke(Object object, Object[] arguments) {
		Object[] targetArguments = transformArguments(arguments);
		return targetMagicMethod.invoke(object, targetArguments);
	}

	protected Object[] transformArguments(Object[] arguments) {
		if (ArrayUtil.isEmpty(arguments)) {
			return arguments;
		}
		Class<?>[] parameterTypes = this.targetMethod.getParameterTypes();
		final int argumentsLength = arguments.length;
		final int parameterTypesLength = parameterTypes.length;
		if (parameterTypesLength != argumentsLength) {
			throw new CanNotFindMethodException("the input arguments size is " + argumentsLength
					+", but method arguments size is" + parameterTypesLength);
		}
		Object[] ret = new Object[argumentsLength];
		for (int i = 0; i < argumentsLength; ++i) {
			Object obj = autoConvertManager.convert(arguments[i], parameterTypes[i]);
			ret[i] = obj;
		}
		return ret;
	}

	@Override
	public MagicList<MagicClass> getParameterTypes() {
		return targetMagicMethod.getParameterTypes();
	}
}
