package wint.lang.exceptions;

import wint.lang.WintException;

public class ValidatorNotFound extends WintException {

	private static final long serialVersionUID = -2730604747095024468L;

	public ValidatorNotFound() {
	}

	public ValidatorNotFound(String message, Throwable cause) {
		super(message, cause);
	}

	public ValidatorNotFound(String message) {
		super(message);
	}

	public ValidatorNotFound(Throwable cause) {
		super(cause);
	}

}
