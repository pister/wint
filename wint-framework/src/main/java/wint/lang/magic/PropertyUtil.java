package wint.lang.magic;

import java.lang.reflect.Method;

import wint.lang.utils.ArrayUtil;
import wint.lang.utils.StringUtil;

/**
 * 属性工具
 * @author pister
 */
public class PropertyUtil {

    /**
     * 获取属性名称
     * @param methodName
     * @return
     */
	public static String getPropertyName(String methodName) {
		if (methodName.startsWith("get") || methodName.startsWith("set")) {
			return StringUtil.lowercaseFirstLetter(methodName.substring(3));
		} else if (methodName.startsWith("is")) {
			return StringUtil.lowercaseFirstLetter(methodName.substring(2));
		}
		return null;
	}

    /**
     * 是否写属性
     * @param method
     * @return
     */
	public static boolean isWritableMethod(Method method) {
		if (method == null) {
			return false;
		}
		if (method.getParameterTypes().length != 1) {
			return false;
		}
		String name = method.getName();
		return name.startsWith("set") && name.length() > 3;
	}

    /**
     * 是否为读属性
     * @param method
     * @return
     */
	public static boolean isReadableMethod(Method method) {
		if (method == null) {
			return false;
		}
		if (!ArrayUtil.isEmpty(method.getParameterTypes())) {
			return false;
		}
		if (method.getReturnType().equals(void.class) || method.getReturnType().equals(Void.class)) {
			return false;
		}
		String name = method.getName();
		if (name.startsWith("get")) {
			return name.length() > 3;
		} else if (name.startsWith("is")) {
			return name.length() > 2;
		}
		return false;
	}
	
}
