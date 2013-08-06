package wint.lang;

/**
 * @author pister 2011-12-22 10:05:48
 */
public class WintException extends RuntimeException {

	private static final long serialVersionUID = -4899890936328064231L;

	public WintException() {
		super();
	}

	public WintException(String message, Throwable cause) {
		super(message, cause);
	}

	public WintException(String message) {
		super(message);
	}

	public WintException(Throwable cause) {
		super(cause);
	}

}
