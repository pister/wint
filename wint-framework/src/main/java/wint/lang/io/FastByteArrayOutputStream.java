package wint.lang.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * @author pister
 * 2011-12-29 04:04:57
 */
public class FastByteArrayOutputStream extends OutputStream {

	protected byte buf[];

	protected int count;

	public FastByteArrayOutputStream() {
		this(32);
	}

	public FastByteArrayOutputStream(int size) {
		if (size < 0) {
			throw new IllegalArgumentException("Negative initial size: " + size);
		}
		buf = new byte[size];
	}

	public void write(int b) {
		int newcount = count + 1;
		if (newcount > buf.length) {
			buf = Arrays.copyOf(buf, Math.max(buf.length << 1, newcount));
		}
		buf[count] = (byte) b;
		count = newcount;
	}

	public void write(byte b[], int off, int len) {
		if ((off < 0) || (off > b.length) || (len < 0) || ((off + len) > b.length) || ((off + len) < 0)) {
			throw new IndexOutOfBoundsException();
		} else if (len == 0) {
			return;
		}
		int newcount = count + len;
		if (newcount > buf.length) {
			buf = Arrays.copyOf(buf, Math.max(buf.length << 1, newcount));
		}
		System.arraycopy(b, off, buf, count, len);
		count = newcount;
	}

	public void writeTo(OutputStream out) throws IOException {
		out.write(buf, 0, count);
	}

	public void reset() {
		count = 0;
	}

	public byte[] toByteArray() {
		return Arrays.copyOf(buf, count);
	}

	public int size() {
		return count;
	}

	public String toString() {
		return new String(buf, 0, count);
	}

	public String toString(String charsetName) throws UnsupportedEncodingException {
		return new String(buf, 0, count, charsetName);
	}

	public void close() throws IOException {
	}

}
