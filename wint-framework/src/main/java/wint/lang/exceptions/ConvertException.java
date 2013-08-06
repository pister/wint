package wint.lang.exceptions;

import wint.lang.WintException;

public class ConvertException extends WintException {

	private static final long serialVersionUID = 7538330651811721720L;

	public ConvertException() {
		super();
	}

	public ConvertException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConvertException(String message) {
		super(message);
	}

	public ConvertException(Throwable cause) {
		super(cause);
	}

}
