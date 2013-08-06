package wint.lang.utils;

public class TextUtil {
	
	public static boolean isNumber(String s) {
		if (StringUtil.isEmpty(s)) {
			return false;
		}
		for (int i = 0, len = s.length(); i < len; ++i) {
			char c = s.charAt(i);
			if (isDigit(c)) {
				continue;
			}
			return false;
		}
		return true;
	}
	
	public static boolean startWithDigit(String s) {
		if (StringUtil.isEmpty(s)) {
			return false;
		}
		char c = s.charAt(0);
		return isDigit(c);
	}
	
	public static boolean isDigit(char c) {
		if (c >= '0' && c <= '9') {
			return true;
		}
		return false;
	}
	
	public static void main(String[] args) {
		System.out.println(isNumber("12a23"));
	}

	
}
