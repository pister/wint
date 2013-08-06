package wint.lang.exceptions;

import wint.lang.WintException;

public class CanNotFindObjectException extends WintException {

	private static final long serialVersionUID = 3828233352416695616L;

	public CanNotFindObjectException() {
	}

	public CanNotFindObjectException(String message, Throwable cause) {
		super(message, cause);
	}

	public CanNotFindObjectException(String message) {
		super(message);
	}

	public CanNotFindObjectException(Throwable cause) {
		super(cause);
	}

}
