package wint.help.mvc.security.csrf;

import wint.help.mvc.WebConstants;

public class TokenNameUtil {

	public static String makeTokenName(String groupName, String tokenName) {
		return WebConstants.CSRF_TOKEN_KEY_PREFIX + groupName + "_" + tokenName;
	}
	
}
