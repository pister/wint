package wint.lang.io;

import java.io.IOException;
import java.io.Writer;

/**
 * @author pister 2011-12-29 03:52:52
 */
public class FastStringWriter extends Writer {

	private StringBuilder buf;

	public FastStringWriter() {
		buf = new StringBuilder();
		lock = buf;
	}

	public FastStringWriter(int initialSize) {
		if (initialSize < 0) {
			throw new IllegalArgumentException("Negative buffer size");
		}
		buf = new StringBuilder(initialSize);
		lock = buf;
	}

	public void write(int c) {
		buf.append((char) c);
	}

	public void write(char cbuf[], int off, int len) {
		if ((off < 0) || (off > cbuf.length) || (len < 0) || ((off + len) > cbuf.length) || ((off + len) < 0)) {
			throw new IndexOutOfBoundsException();
		} else if (len == 0) {
			return;
		}
		buf.append(cbuf, off, len);
	}

	public void write(String str) {
		buf.append(str);
	}

	public void write(String str, int off, int len) {
		buf.append(str.substring(off, off + len));
	}

	public FastStringWriter append(CharSequence csq) {
		if (csq == null)
			write("null");
		else
			write(csq.toString());
		return this;
	}

	public FastStringWriter append(CharSequence csq, int start, int end) {
		CharSequence cs = (csq == null ? "null" : csq);
		write(cs.subSequence(start, end).toString());
		return this;
	}

	public FastStringWriter append(char c) {
		write(c);
		return this;
	}

	public String toString() {
		return buf.toString();
	}

	public StringBuilder getBuffer() {
		return buf;
	}

	public void flush() {
	}

	public void close() throws IOException {
	}

}
