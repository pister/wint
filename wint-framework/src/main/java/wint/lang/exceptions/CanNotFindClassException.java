package wint.lang.exceptions;


/**
 * @author pister 2011-12-22 10:05:31
 */
public class CanNotFindClassException extends MagicException {

	private static final long serialVersionUID = 3223099941081750645L;

	public CanNotFindClassException() {
	}

	public CanNotFindClassException(String message, Throwable cause) {
		super(message, cause);
	}

	public CanNotFindClassException(String message) {
		super(message);
	}

	public CanNotFindClassException(Throwable cause) {
		super(cause);
	}

}
