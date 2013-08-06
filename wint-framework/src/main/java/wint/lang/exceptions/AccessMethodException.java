package wint.lang.exceptions;


/**
 * @author pister 2011-12-22 10:05:27
 */
public class AccessMethodException extends MagicException {

	private static final long serialVersionUID = -2100441222208149924L;

	public AccessMethodException() {
	}

	public AccessMethodException(String message, Throwable cause) {
		super(message, cause);
	}

	public AccessMethodException(String message) {
		super(message);
	}

	public AccessMethodException(Throwable cause) {
		super(cause);
	}

}
