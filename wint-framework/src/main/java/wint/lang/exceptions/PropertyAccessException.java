package wint.lang.exceptions;

import wint.lang.WintException;

public class PropertyAccessException extends WintException {

	private static final long serialVersionUID = -2053413110258751349L;

	public PropertyAccessException() {
		super();
	}

	public PropertyAccessException(String message, Throwable cause) {
		super(message, cause);
	}

	public PropertyAccessException(String message) {
		super(message);
	}

	public PropertyAccessException(Throwable cause) {
		super(cause);
	}

}
