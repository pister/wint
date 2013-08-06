package wint.maven.plugins.gen.common.util;

public class StringUtil {

	public static final String EMPTY = "";

	public static boolean isEmpty(String s) {
		if (s == null || s.length() == 0) {
			return true;
		}
		return false;
	}

	public static String trimToEmpty(String input) {
		if (input == null) {
			return EMPTY;
		}
		return input.trim();
	}

}
