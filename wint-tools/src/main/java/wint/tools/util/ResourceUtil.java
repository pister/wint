package wint.tools.util;

import java.io.InputStream;

public class ResourceUtil {
	
	public static InputStream getResourceStream(String name) {
		InputStream is = ResourceUtil.class.getResourceAsStream(name);
		if (is != null) {
			return is;
		}
		is = Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
		if (is != null) {
			return is;
		}
		is = ResourceUtil.class.getClassLoader().getResourceAsStream(name);
		if (is != null) {
			return is;
		}
		return is;
	}

}
