package wint.lang.logback;

import ch.qos.logback.core.rolling.RollingFileAppender;

/**
 * @author pister
 *
 * @param <E>
 */
public class WintRollingFileAppender<E> extends RollingFileAppender<E> {

	@Override
	public synchronized void setFile(String file) {
		file = LogbackPathUtil.ensureDir(file);
		super.setFile(file);
	}

}
