package wint.lang.exceptions;

import wint.lang.WintException;

public class ParseException extends WintException {

	private static final long serialVersionUID = 2276934769182564511L;

	public ParseException() {
	}

	public ParseException(String message, Throwable cause) {
		super(message, cause);
	}

	public ParseException(String message) {
		super(message);
	}

	public ParseException(Throwable cause) {
		super(cause);
	}

}
