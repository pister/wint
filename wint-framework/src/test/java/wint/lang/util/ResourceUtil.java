package wint.lang.util;

import java.io.InputStream;

public class ResourceUtil {
	
	public static InputStream getInputStream(String name) {
		InputStream is = ResourceUtil.class.getResourceAsStream(name);
		if (is != null) {
			return is;
		}
		return ResourceUtil.class.getClassLoader().getResourceAsStream(name);
	}

}
