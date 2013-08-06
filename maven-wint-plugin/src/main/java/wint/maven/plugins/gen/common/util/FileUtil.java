package wint.maven.plugins.gen.common.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtil {

	public static void writeContent(File file, byte[] bytes) throws IOException {
		FileOutputStream os = new FileOutputStream(file);
		os.write(bytes);
		os.close();
	}
	
}
