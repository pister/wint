package wint.lang.utils;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

public class IoUtil {
	
	private static final int CHAR_BUF_LEN = 1024 * 2;
	private static final int BYTE_BUF_LEN = 1024 * 4;

    private static ThreadLocal<byte[]> byteArrayBufTl = new ThreadLocal<byte[]>() {
        @Override
        protected byte[] initialValue() {
            return new byte[BYTE_BUF_LEN];
        }
    };

    private static ThreadLocal<char[]> charArrayBufTl = new ThreadLocal<char[]>() {
        @Override
        protected char[] initialValue() {
            return new char[CHAR_BUF_LEN];
        }
    };

	public static void copy(InputStream is, OutputStream os) throws IOException {
		byte[] buf = byteArrayBufTl.get();
		while (true) {
			int len = is.read(buf, 0, BYTE_BUF_LEN);
			if (len < 0) {
				break;
			}
			os.write(buf, 0, len);
		}
	}
	
	public static void copyAndClose(InputStream is, OutputStream os) throws IOException {
		copy(is, os);
		close(is);
		close(os);
	}
	
	public static void copy(Reader reader, Writer writer) throws IOException {
		char[] buf = charArrayBufTl.get();
		while (true) {
			int len = reader.read(buf, 0, CHAR_BUF_LEN);
			if (len < 0) {
				break;
			}
			writer.write(buf, 0, len);
		}
	}
	
	public static void copyAndClose(Reader reader, Writer writer) throws IOException {
		copy(reader, writer);
		close(reader);
		close(writer);
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
