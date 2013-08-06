package wint.lang.io;

import wint.lang.utils.StringUtil;

public class FileNameUtil {
	
	/**
	 * @param fileName
	 * @return
	 */
	public static String getFileNameSuffix(String fileName) {
		if (StringUtil.isEmpty(fileName)) {
			return null;
		}
		int pos = fileName.lastIndexOf('.');
		if (pos < 0 ) {
			return null;
		}
		return fileName.substring(pos + 1);
	}
	
}
