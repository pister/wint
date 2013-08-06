package wint.session.servlet;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletOutputStream;

public class DelegatingServletOutputStream extends ServletOutputStream {

	private final OutputStream targetStream;
	
	public DelegatingServletOutputStream(OutputStream targetStream) {
		super();
		this.targetStream = targetStream;
	}

	@Override
	public void write(byte[] b) throws IOException {
		targetStream.write(b);
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		targetStream.write(b, off, len);
	}
	
	public void write(int b) throws IOException {
		this.targetStream.write(b);
	}

	public void flush() throws IOException {
		super.flush();
		this.targetStream.flush();
	}

	public void close() throws IOException {
		super.close();
		this.targetStream.close();
	}

	

}
