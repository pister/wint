package wint.lang.exceptions;

import wint.lang.WintException;

/**
 * @author pister 2012-3-2 08:35:47
 */
public class UrlException extends WintException {

	private static final long serialVersionUID = -5225236375159307740L;

	public UrlException() {
	}

	public UrlException(String message, Throwable cause) {
		super(message, cause);
	}

	public UrlException(String message) {
		super(message);
	}

	public UrlException(Throwable cause) {
		super(cause);
	}

}
