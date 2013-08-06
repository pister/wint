package wint.lang.exceptions;

import wint.lang.WintException;

public class ResourceException extends WintException {

	private static final long serialVersionUID = -7615296012736364519L;

	public ResourceException() {
		super();
	}

	public ResourceException(String message, Throwable cause) {
		super(message, cause);
	}

	public ResourceException(String message) {
		super(message);
	}

	public ResourceException(Throwable cause) {
		super(cause);
	}

}
