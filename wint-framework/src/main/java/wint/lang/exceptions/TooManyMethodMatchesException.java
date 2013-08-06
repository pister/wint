package wint.lang.exceptions;

/**
 * @author pister
 * 2011-12-23 10:35:06
 */
public class TooManyMethodMatchesException extends MagicException {

	private static final long serialVersionUID = -6989550061167592760L;

	public TooManyMethodMatchesException() {
		super();
	}

	public TooManyMethodMatchesException(String message, Throwable cause) {
		super(message, cause);
	}

	public TooManyMethodMatchesException(String message) {
		super(message);
	}

	public TooManyMethodMatchesException(Throwable cause) {
		super(cause);
	}

}
