package wint.lang.exceptions;

import wint.lang.WintException;

public class CanNotFindFormException extends WintException {

	private static final long serialVersionUID = 4701032846322493376L;

	public CanNotFindFormException() {
		super();
	}

	public CanNotFindFormException(String message, Throwable cause) {
		super(message, cause);
	}

	public CanNotFindFormException(String message) {
		super(message);
	}

	public CanNotFindFormException(Throwable cause) {
		super(cause);
	}

}
