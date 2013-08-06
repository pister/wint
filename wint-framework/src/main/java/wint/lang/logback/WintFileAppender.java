package wint.lang.logback;

import java.io.File;

import ch.qos.logback.core.FileAppender;

/**
 * @author pister 2012-3-3 09:19:45
 * @param <E>
 */
public class WintFileAppender<E> extends FileAppender<E> {

	@Override
	public synchronized void setFile(String file) {
		File targetFile = new File(file);
		File parentFile = targetFile.getParentFile();
		parentFile.mkdirs();
		super.setFile(file);
	}


}
