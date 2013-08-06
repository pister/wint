package wint.maven.plugins.gen.common.util;

import java.io.InputStream;

/**
 * @author pister 2011-12-22 10:05:52
 */
public class ClassUtil {
	
	public static final char RESOURCE_SEPARATOR_CHAR = '/';

	public static final char PACKAGE_SEPARATOR_CHAR = '.';

	public static final String PACKAGE_SEPARATOR = String.valueOf(PACKAGE_SEPARATOR_CHAR);

	public static final char INNER_CLASS_SEPARATOR_CHAR = '$';

	public static final String INNER_CLASS_SEPARATOR = String.valueOf(INNER_CLASS_SEPARATOR_CHAR);
	
	

	public static InputStream getResourceAsStream(Class<?> clazz, String name) {
		InputStream is = clazz.getResourceAsStream(name);
		if (is != null) {
			return is;
		}
		is = clazz.getClassLoader().getResourceAsStream(name);
		if (is != null) {
			return is;
		}
		return null;
	}
	
	public static ClassLoader getClassLoader() {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		if (classLoader != null) {
			return classLoader;
		}
		return ClassUtil.class.getClassLoader();
	}

	
	public static Class<?> forName(String name) {
		return forName(name, getClassLoader());
	}
	
	public static Class<?> forName(String name, ClassLoader classLoader) {
		try {
			return Class.forName(name, true, classLoader);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String getShortClassName(String className) {
		if (StringUtil.isEmpty(className)) {
			return className;
		}

		className = getClassName(className, false);

		char[] chars = className.toCharArray();
		int lastDot = 0;

		for (int i = 0; i < chars.length; i++) {
			if (chars[i] == PACKAGE_SEPARATOR_CHAR) {
				lastDot = i + 1;
			} else if (chars[i] == INNER_CLASS_SEPARATOR_CHAR) {
				chars[i] = PACKAGE_SEPARATOR_CHAR;
			}
		}

		return new String(chars, lastDot, chars.length - lastDot);
	}

	public static String getShortClassName(Class<?> clazz) {
		if (clazz == null) {
			return null;
		}
		return getShortClassName(clazz.getName());
	}
	
	private static String getClassName(String className, boolean processInnerClass) {
		if (StringUtil.isEmpty(className)) {
			return className;
		}

		if (processInnerClass) {
			className = className.replace(INNER_CLASS_SEPARATOR_CHAR, PACKAGE_SEPARATOR_CHAR);
		}

		int length = className.length();
		int dimension = 0;

		for (int i = 0; i < length; i++, dimension++) {
			if (className.charAt(i) != '[') {
				break;
			}
		}
		if (dimension == 0) {
			return className;
		}

		if (length <= dimension) {
			return className;
		}
		StringBuilder componentTypeName = new StringBuilder();

		switch (className.charAt(dimension)) {
		case 'Z':
			componentTypeName.append("boolean");
			break;
		case 'B':
			componentTypeName.append("byte");
			break;
		case 'C':
			componentTypeName.append("char");
			break;
		case 'D':
			componentTypeName.append("double");
			break;
		case 'F':
			componentTypeName.append("float");
			break;
		case 'I':
			componentTypeName.append("int");
			break;
		case 'J':
			componentTypeName.append("long");
			break;
		case 'S':
			componentTypeName.append("short");
			break;
		case 'L':
			if ((className.charAt(length - 1) != ';') || (length <= (dimension + 2))) {
				return className;
			}
			componentTypeName.append(className.substring(dimension + 1, length - 1));
			break;

		default:
			return className;
		}
		for (int i = 0; i < dimension; i++) {
			componentTypeName.append("[]");
		}
		return componentTypeName.toString();
	}

	public static String getClassName(String className) {
		return getClassName(className, true);
	}
	
}
