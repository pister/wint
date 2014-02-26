package wint.sessionx.cookie;

import org.apache.commons.lang.time.FastDateFormat;

import javax.servlet.http.Cookie;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class CookieUtil {
	
	private static final TimeZone GMT_TIME_ZONE = TimeZone.getTimeZone("GMT");

	private static final String COOKIE_DATE_PATTERN = "EEE, dd-MMM-yyyy HH:mm:ss 'GMT'";
	
	private static final FastDateFormat DATE_FORMAT = FastDateFormat.getInstance(COOKIE_DATE_PATTERN, GMT_TIME_ZONE, Locale.US);
	
	public static final String EXPIRES = "Expires";
	public static final String PATH = "Path";
	public static final String DOMAIN = "Domain";
	public static final String HTTP_ONLY = "HttpOnly";
	public static final String COOKIE_SEPARATOR = ";";
	public static final String KEY_VALUE_SEPARATOR = "=";
	public static final String SET_COOKIE = "Set-Cookie";
	public static final String COOKIE = "Cookie";
	
	public static String buildHttpOnlyCookieString(Cookie cookie) {
		return buildCookieString(cookie, true);
	}
	
	public static String buildCookieString(Cookie cookie, boolean httpOnly) {
		StringBuilder cookieBuilder = new StringBuilder();
		cookieBuilder.append(cookie.getName()).append(KEY_VALUE_SEPARATOR).append(cookie.getValue());
		cookieBuilder.append(COOKIE_SEPARATOR);

		if (cookie.getDomain() != null) {
			cookieBuilder.append(DOMAIN).append(KEY_VALUE_SEPARATOR).append(cookie.getDomain());
			cookieBuilder.append(COOKIE_SEPARATOR);
		}

		if (cookie.getPath() != null) {
			cookieBuilder.append(PATH).append(KEY_VALUE_SEPARATOR).append(cookie.getPath());
			cookieBuilder.append(COOKIE_SEPARATOR);
		}
        /*
		if (cookie.getMaxAge() >= 0) {
			cookieBuilder.append(EXPIRES).append(KEY_VALUE_SEPARATOR).append(getCookieExpires(cookie));
			cookieBuilder.append(COOKIE_SEPARATOR);
		}
		  */
		if (httpOnly) {
			cookieBuilder.append(HTTP_ONLY);
		}
		return cookieBuilder.toString();
	}


	public static String getCookieExpires(Cookie cookie) {
		String result = null;
		int maxAge = cookie.getMaxAge();
		if (maxAge > 0) {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.SECOND, maxAge);
			result = DATE_FORMAT.format(calendar);
		} else { // maxAge == 0
			result = DATE_FORMAT.format(0); // maxAge为0时表示需要删除该cookie，因此将时间设为最小时间，即1970年1月1日
		}
		return result;
	}
	
}
