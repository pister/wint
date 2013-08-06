package wint.lang.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import wint.lang.WintException;
import wint.lang.io.FastByteArrayOutputStream;

public class StreamUtil {
	
	public static byte[] read(InputStream is) throws IOException {
		FastByteArrayOutputStream bos = new FastByteArrayOutputStream();
		IoUtil.copy(is, bos);
		IoUtil.close(is);
		IoUtil.close(bos);
		return bos.toByteArray();
	}
	
	public static String readAsString(InputStream is, String charset) throws IOException {
		return new String(read(is), charset);
	}
	
	public static String readAsString(InputStream is) throws IOException {
		return new String(read(is));
	}
	
	public static InputStream getUserHomeResourceAsStream(String name) {
		String userHome = System.getProperty("user.home");
		if (userHome == null) {
			return null;
		}
		File userHomeFile = new File(userHome);
		if (!userHomeFile.exists()) {
			return null;
		}
		File targetFile = new File(userHomeFile, name);
		if (!targetFile.exists()) {
			return null;
		}
		try {
			return new FileInputStream(targetFile);
		} catch (FileNotFoundException e) {
			throw new WintException(e);
		}
	}

}
