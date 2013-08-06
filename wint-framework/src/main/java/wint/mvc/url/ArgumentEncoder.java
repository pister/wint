package wint.mvc.url;

import java.util.regex.Pattern;

import wint.lang.utils.StringUtil;

public class ArgumentEncoder {
	
	static Pattern SPACE_PATTERN = Pattern.compile("[\\s]+?");
	
	static Pattern SLASH_CHARS_PATTERN = Pattern.compile("[\\/\\&\\\\\\-\\_\\?\\.\\,]+?");
	
	public static String transformStringArgument(Object argument) {
		if (argument == null) {
			return StringUtil.EMPTY;
		}
		String s = argument.toString();
		
		s = SPACE_PATTERN.matcher(s).replaceAll("+");
		return SLASH_CHARS_PATTERN.matcher(s).replaceAll("");
		
	}

}
