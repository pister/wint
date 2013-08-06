package wint.mvc.servlet.mock;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;

public class PrintWriterMock extends PrintWriter {

	public PrintWriterMock(OutputStream out) {
		super(out);
	}

	public PrintWriterMock(Writer out) {
		super(out);
	}
	
	public PrintWriterMock() {
		this(System.out);
	}

	@Override
	public void close() {
		this.flush();
	}

}
