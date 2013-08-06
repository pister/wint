package wint.lang.logback;

import java.io.File;

import ch.qos.logback.core.rolling.RollingFileAppender;

/**
 * @author pister
 *
 * @param <E>
 */
public class WintRollingFileAppender<E> extends RollingFileAppender<E> {

	@Override
	public synchronized void setFile(String file) {
		File targetFile = new File(file);
		File parentFile = targetFile.getParentFile();
		parentFile.mkdirs();
		super.setFile(file);
	}

}
