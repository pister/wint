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

    public static boolean isNumeric(String s) {
        if (StringUtil.isEmpty(s)) {
            return false;
        }
        for (int i = 0, len = s.length(); i < len; ++i) {
            char c = s.charAt(i);
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println(isNumeric("abcd"));
        System.out.println(isNumeric("abcd123"));
        System.out.println(isNumeric("1234"));
        System.out.println(isNumeric("123avc"));
        System.out.println(isNumeric("-123"));
        System.out.println(isNumeric("123.456"));
    }
	
}
