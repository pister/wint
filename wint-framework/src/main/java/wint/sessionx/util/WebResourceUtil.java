package wint.sessionx.util;

import wint.lang.utils.ClassUtil;

import java.io.InputStream;

public class WebResourceUtil {
	
	public static InputStream getWebResouceAsStream(String name) {
		InputStream is = ClassUtil.getResourceAsStream(name);
		if (is != null) {
			return is;
		}
		return ClassUtil.getResourceAsStream("WEB-INF/classes/" + name);
	}

}
