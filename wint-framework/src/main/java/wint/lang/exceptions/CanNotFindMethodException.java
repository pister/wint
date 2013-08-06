package wint.lang.exceptions;


/**
 * @author pister 2011-12-22 10:05:37
 */
public class CanNotFindMethodException extends MagicException {

	private static final long serialVersionUID = -1550914706009777296L;

	public CanNotFindMethodException() {
	}

	public CanNotFindMethodException(String message, Throwable cause) {
		super(message, cause);
	}

	public CanNotFindMethodException(String message) {
		super(message);
	}

	public CanNotFindMethodException(Throwable cause) {
		super(cause);
	}

}
