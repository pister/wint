package wint.session.cookie;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;

import wint.lang.utils.CollectionUtil;
import wint.lang.utils.MapUtil;

public class CookiesHelper {
	
	private Map<String, List<Cookie>> namedCookies = MapUtil.newHashMap();
	
	public CookiesHelper(List<Cookie> cookies) {
		setCookies(cookies);
	}
	
	public CookiesHelper(Cookie[] cookies) {
		if (cookies == null) {
			return;
		}
		setCookies(Arrays.asList(cookies));
	}
	
	private void setCookies(List<Cookie> cookies) {
		if (cookies == null) {
			return;
		}
		for (Cookie cookie : cookies) {
			String name = cookie.getName();
			List<Cookie> cookieList = namedCookies.get(name);
			if (cookieList == null) {
				cookieList = CollectionUtil.newArrayList();
				namedCookies.put(name, cookieList);
			}
			cookieList.add(cookie);
		}
	}
	
	public Cookie getCookie(String name) {
		List<Cookie> cookies = getCookies(name);
		if (CollectionUtil.isEmpty(cookies)) {
			return null;
		}
		return cookies.get(0);
	}
	
	public Set<String> getNames() {
		return namedCookies.keySet();
	}
	
	public List<Cookie> getCookies(String name) {
		return namedCookies.get(name);
	}
	
}
