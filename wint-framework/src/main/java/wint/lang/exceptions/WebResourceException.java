package wint.lang.exceptions;

import wint.lang.WintException;

public class WebResourceException extends WintException {

	private static final long serialVersionUID = 3370302536929702656L;

	public WebResourceException() {
		super();
	}

	public WebResourceException(String message, Throwable cause) {
		super(message, cause);
	}

	public WebResourceException(String message) {
		super(message);
	}

	public WebResourceException(Throwable cause) {
		super(cause);
	}

}
