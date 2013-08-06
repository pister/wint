package wint.lang.exceptions;

import wint.lang.WintException;

public class ViewException extends WintException {

	private static final long serialVersionUID = 2545454546030277458L;

	public ViewException() {
	}

	public ViewException(String message, Throwable cause) {
		super(message, cause);
	}

	public ViewException(String message) {
		super(message);
	}

	public ViewException(Throwable cause) {
		super(cause);
	}

}
