package wint.session.config;

import java.io.InputStream;

import wint.lang.utils.ClassUtil;

public class WebResourceUtil {
	
	public static InputStream getWebResouceAsStream(String name) {
		InputStream is = ClassUtil.getResourceAsStream(name);
		if (is != null) {
			return is;
		}
		return ClassUtil.getResourceAsStream("WEB-INF/classes/" + name);
	}

}
