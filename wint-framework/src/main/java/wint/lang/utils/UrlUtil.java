package wint.lang.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

import wint.lang.WintException;

public class UrlUtil {
	
	public static String encode(String name, String charset) {
		try {
			if (StringUtil.isEmpty(name)) {
				return name;
			}
			return URLEncoder.encode(name, charset);
		} catch (UnsupportedEncodingException e) {
			throw new WintException(e);
		}
	}
	
	public static String decode(String name, String charset) {
		try {
			if (StringUtil.isEmpty(name)) {
				return name;
			}
			return URLDecoder.decode(name, charset);
		} catch (UnsupportedEncodingException e) {
			throw new WintException(e);
		}
	}
	
	public static String getRequestURL(HttpServletRequest request) {
		StringBuffer requestURL = request.getRequestURL();
		String queryString = request.getQueryString();
		if (!StringUtil.isEmpty(queryString)) {
			requestURL.append("?");
			requestURL.append(queryString);
		}
		return requestURL.toString();
	}

}
