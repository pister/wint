package wint.lang.log4j;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author pister 2010-3-8
 */
public class DailyRollingFileAppender extends org.apache.log4j.DailyRollingFileAppender {

	@Override
	public synchronized void setFile(String fileName, boolean append, boolean bufferedIO, int bufferSize) throws IOException {
		new File(fileName).getParentFile().mkdirs();
		super.setFile(fileName, append, bufferedIO, bufferSize);
	}

}
