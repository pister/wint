package wint.lang.utils;

public class ObjectUtil {
	
	public static boolean equals(Object o1, Object o2) {
		if (o1 == o2) {
			return true;
		}
		if (o1 == null || o2 == null) {
			return false;
		}
		return o1.equals(o2);
	}

    public static String toString(Object o) {
        if (o == null) {
            return null;
        }
        return o.toString();
    }

}
