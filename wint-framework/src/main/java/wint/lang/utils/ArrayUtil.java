package wint.lang.utils;

import java.lang.reflect.Array;

import wint.lang.magic.MagicList;

/**
 * @author pister 2011-12-22 10:11:30
 */
public class ArrayUtil {
	
	public static final Class<?>[] EMPTY_CLASS_ARRAY = new Class<?>[0];
	
	public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];
	
	public static boolean isEmpty(Object[] array) {
		if (array == null || array.length == 0) {
			return true;
		}
		return false;
	}
	
	public static int getLength(Object array) {
		if (array == null) {
			return 0;
		}
		if (!array.getClass().isArray()) {
			return 0;
		}
		return Array.getLength(array);
	}
	
	public static int getLength(Object[] array) {
		if (array == null || array.length == 0) {
			return 0;
		}
		return array.length;
	}
	
	public static String toString(Object[] array) {
		return "[" + join(array, ",") + "]";
	}
	
	public static String join(Object[] array, String token) {
		return MagicList.wrap(array).join(token);
	}

    public static String join(Object array, String token) {
        return MagicList.wrap(array).join(token);

    }

}
