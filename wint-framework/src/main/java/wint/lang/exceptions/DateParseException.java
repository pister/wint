package wint.lang.exceptions;

import wint.lang.WintException;

public class DateParseException extends WintException {

	private static final long serialVersionUID = 5065084266979004996L;

	public DateParseException() {
		super();
	}

	public DateParseException(String message, Throwable cause) {
		super(message, cause);
	}

	public DateParseException(String message) {
		super(message);
	}

	public DateParseException(Throwable cause) {
		super(cause);
	}

}
