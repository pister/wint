package wint.lang.utils;

import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;


public class ClassLoaderUtil  {
	
	private static final Class<?>[] DEFINE_CLASS_ARG_TYPES = new Class<?>[] { String.class, new byte[0].getClass(), Integer.TYPE, Integer.TYPE };

	private static Method DEFINE_CLASS_METHOD;

	static {
		// Class<?> defineClass(String name, byte[] b, int off, int len)
		DEFINE_CLASS_METHOD = AccessController.doPrivileged(new PrivilegedAction<Method>() {
			
			public Method run() {
				try {
					Class<?> clazz = Class.forName("java.lang.ClassLoader");
					Method defineMethod = clazz.getDeclaredMethod("defineClass", DEFINE_CLASS_ARG_TYPES);
					defineMethod.setAccessible(true);
					return defineMethod;
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}

		});
	}
	
	public static Class<?> defineClass(ClassLoader classLoader, String className, byte[] data, int pos, int len) {
		try {
			DEFINE_CLASS_METHOD.invoke(classLoader, new Object[] { className, data, 0, data.length });
			return Class.forName(className, true, classLoader);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
