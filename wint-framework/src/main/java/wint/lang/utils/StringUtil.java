package wint.lang.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class StringUtil {

	public static final String EMPTY = "";
	
	public static boolean isEmpty(String s) {
		if (s == null || s.length() == 0) {
			return true;
		}
		return false;
	}

    public static boolean isNotEmpty(String s) {
        return !isEmpty(s);
    }
	
	public static boolean isBlank(String s) {
        int strLen;
        if (s == null || (strLen = s.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(s.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotBlank(String s) {
        return !isBlank(s);
    }
	
	public static String trimToEmpty(String input) {
		if (input == null) {
			return EMPTY;
		}
		return input.trim();
	}
	
	public static String trimToSize(String input, int size) {
		if (input == null) {
			return EMPTY;
		}
		if (input.length() < size) {
			return input;
		}
		return input.substring(0, size);
	}
	
	/**
	 * 
	 * @param str
	 * @return
	 */
	public static String uppercaseFirstLetter(String str) {
		if (StringUtil.isEmpty(str)) {
			return str;
		}
		char firstLetter = str.charAt(0);
		firstLetter = Character.toUpperCase(firstLetter);
		return firstLetter + str.substring(1);
	}

	/**
	 * 
	 * @param str
	 * @return
	 */
	public static String lowercaseFirstLetter(String str) {
		if (StringUtil.isEmpty(str)) {
			return str;
		}
		char firstLetter = str.charAt(0);
		firstLetter = Character.toLowerCase(firstLetter);
		return firstLetter + str.substring(1);
	}

	public static String replace(String inString, String oldPattern, String newPattern) {
		if (isEmpty(inString) || isEmpty(oldPattern) || newPattern == null) {
			return inString;
		}
		StringBuilder sbuf = new StringBuilder();
		int pos = 0;
		int index = inString.indexOf(oldPattern);
		int patLen = oldPattern.length();
		while (index >= 0) {
			sbuf.append(inString.substring(pos, index));
			sbuf.append(newPattern);
			pos = index + patLen;
			index = inString.indexOf(oldPattern, pos);
		}
		sbuf.append(inString.substring(pos));
		return sbuf.toString();
	}
	
	public static String camelToFixedString(String str, String fixed) {
		str = trimToEmpty(str);
		if (isEmpty(str)) {
			return str;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < str.length(); ++i) {
			char c = str.charAt(i);
			if (Character.isUpperCase(c)) {
				if(i != 0) {
					sb.append(fixed);
				}
				sb.append(Character.toLowerCase(c));
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}
	
	/**
	 * null		=>		""
	 * "	"	=>		""
	 * "abc"	=>		"abc"
	 * "aBc"	=>		"a_bc"
	 * "helloWord"	=>	"hello_word"
	 * "hi hao are   you"	=> "hi hao are   you"
	 * "helloWord_iAmHsl"	=>	"hello_word_i_am_hsl"
	 * @return
	 */
	public static String camelToUnderLineString(String str) {
		return camelToFixedString(str, "_");
	}
	
	
	/**
	 * null		=>		""
	 * "	"	=>		""
	 * "abc"	=>		"abc"
	 * "a_bc"	=>		"aBc"
	 * "the_first_name_"	=>		"theFirstName"
	 * "hello_word"	=>	"helloWord"
	 * "hi hao are   you"	=> "hi hao are   you"
	 * "hello_word_i_am_hsl"	=>	"helloWordIAmHsl"
	 * @return
	 */
	public static String underLineStringToCamel(String str) {
		return fixedCharToCamel(str, '_');
	}
	
	public static String fixedCharToCamel(String str, String fixedChars) {
		if (StringUtil.isEmpty(fixedChars)) {
			return str;
		}
		Set<Character> fixedCharSet = CollectionUtil.newHashSet();
		for (int i = 0, len = fixedChars.length(); i < len; ++i) {
			fixedCharSet.add(fixedChars.charAt(i));
		}
		return fixedCharToCamel(str, fixedCharSet);
	}

	public static String fixedCharToCamel(String str, char fixedChar) {
		Set<Character> fixedCharSet = CollectionUtil.newHashSet();
		fixedCharSet.add(fixedChar);
		return fixedCharToCamel(str, fixedCharSet);
	}
	
	private static String fixedCharToCamel(String str, Set<Character> fixedChars) {
		str = trimToEmpty(str);
		if (isEmpty(str)) {
			return str;
		}
		StringBuilder sb = new StringBuilder();
		final int len = str.length();
		for (int i = 0; i < len; ++i) {
			char c = str.charAt(i);
			if (fixedChars.contains(c)) {
				++i;
				if (i != len) {
					c = Character.toUpperCase(str.charAt(i));
					sb.append(c);
				}
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}
	
	/**
	 * @param input
	 * @param token
	 * @return
	 */
	public static String getFirstBefore(String input, String token) {
		if (isEmpty(input)) {
			return input;
		}
		int pos = input.indexOf(token);
		if (pos < 0) {
			return input;
		}
		return input.substring(0, pos);
	}
	
	public static String getFirstAfter(String input, String token) {
		if (isEmpty(input)) {
			return input;
		}
		int pos = input.indexOf(token);
		if (pos < 0) {
			return input;
		}
		return input.substring(pos + token.length());
	}
	
	/**
	 * @param input
	 * @param input
	 * @param token
	 * @return
	 */
	public static String getLastBefore(String input, String token) {
		if (isEmpty(input)) {
			return input;
		}
		int pos = input.lastIndexOf(token);
		if (pos < 0) {
			return input;
		}
		return input.substring(0, pos);
	}
	
	public static String getLastAfter(String input, String token) {
		if (isEmpty(input)) {
			return input;
		}
		int pos = input.lastIndexOf(token);
		if (pos < 0) {
			return input;
		}
		return input.substring(pos + token.length());
	}
	

	/**
	 * @param input
	 * @param regex
	 * @return
	 */
	public static List<String> splitTrim(String input, String regex) {
        if (isEmpty(input)) {
            return CollectionUtil.newArrayList(0);
        }
		String[] parts = input.split(regex);
        List<String> ret = new ArrayList<String>(parts.length);
        for (String part : parts) {
			String trimmedPart = StringUtil.trimToEmpty(part);
			if (StringUtil.isEmpty(trimmedPart)) {
				continue;
			}
			ret.add(trimmedPart);
		}
		return ret;
	}
	
	public static boolean equals(String s1, String s2) {
		if (s1 == s2) {
			return true;
		}
		if (s1 == null || s2 == null) {
			return false;
		}
		return s1.equals(s2);
	}
	
}
