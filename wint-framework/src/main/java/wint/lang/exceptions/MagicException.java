package wint.lang.exceptions;

import wint.lang.WintException;

/**
 * @author pister
 * 2011-12-23 10:33:37
 */
public class MagicException extends WintException {

	private static final long serialVersionUID = 3754251068770045184L;

	public MagicException() {
	}

	public MagicException(String message, Throwable cause) {
		super(message, cause);
	}

	public MagicException(String message) {
		super(message);
	}

	public MagicException(Throwable cause) {
		super(cause);
	}

}
