package wint.tools.util;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

public class IoUtil {
	
	static final int BUF_SIZE = 1024 * 8;
	
	public static String readToString(Reader reader) {
		try {
			StringWriter stringWriter = new StringWriter();
			copyAndClose(reader, stringWriter);
			return stringWriter.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void copy(InputStream is, OutputStream os) throws IOException {
		byte[] buf = new byte[BUF_SIZE];
		while (true) {
			int len = is.read(buf);
			if (len < 0) {
				break;
			}
			os.write(buf, 0, len);
		}
	}
	
	public static void copyAndClose(InputStream is, OutputStream os) throws IOException {
		try {
			copy(is, os);
		} finally {
			close(is);
			close(os);
		}
	}
	public static void copyAndClose(Reader reader, Writer writer) throws IOException {
		try {
			copy(reader, writer);
		} finally {
			close(reader);
			close(writer);
		}
	}
	
	public static void copy(Reader reader, Writer writer) throws IOException {
		char[] buf = new char[BUF_SIZE];
		while (true) {
			int len = reader.read(buf);
			if (len < 0) {
				break;
			}
			writer.write(buf, 0, len);
		}
	}
	
	public static void close(Closeable c) {
		if (c != null) {
			try {
				c.close();
			} catch (IOException e) {
				// ignore
			}
		}
	}

}
