package wint.sessionx.util;

public interface WintSessionContants {
	
	int WINT_COOKIE_SESSION_EXPIRE = 60 * 30;
	
	String WINT_COOKIE_SESSION_DATA_NAME_PREFIX = "s_data_";
	
	String WINT_COOKIE_SESSION_DOMAIN = "";
	
	int WINT_COOKIE_DATA_MAX_SIZE = 1024 * 2;
	
	String WINT_COOKIE_MAGIC_TOKEN = "wint:";
	
	interface InnerKeys {
		
		String INNER_PRE = "_wi_";
		
		String CREATE_TIME_KEY = INNER_PRE + "ct";

	}

}
