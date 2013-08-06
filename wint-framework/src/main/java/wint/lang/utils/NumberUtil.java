package wint.lang.utils;

public class NumberUtil {

	/**
	 * @param obj
	 * @return
	 */
	public static boolean isPositiveInteger(Object obj) {
		if (obj instanceof Long) {
			return true;
		}
		if (obj instanceof Integer) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		String str = obj.toString();
		for (int i = 0, len = str.length(); i < len; ++i) {
			char c = str.charAt(i);
			if (c >= '0' && c <= '9') {
				continue;
			}
			return false;
		}
		return true;
	}
	
}
