package wint.mvc.servlet.mock;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletOutputStream;

public class ServletOutputStreamMock extends ServletOutputStream {

	private OutputStream os;
	
	public ServletOutputStreamMock(OutputStream os) {
		super();
		this.os = os;
	}
	public ServletOutputStreamMock() {
		this(System.out);
	}

	@Override
	public void write(int b) throws IOException {
		os.write(b);
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		os.write(b, off, len);
	}
	
	@Override
	public void close() {
		try {
			this.flush();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
