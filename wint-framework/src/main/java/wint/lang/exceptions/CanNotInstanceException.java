package wint.lang.exceptions;

public class CanNotInstanceException extends MagicException {

	private static final long serialVersionUID = 573187782095123825L;

	public CanNotInstanceException() {
	}

	public CanNotInstanceException(String message, Throwable cause) {
		super(message, cause);
	}

	public CanNotInstanceException(String message) {
		super(message);
	}

	public CanNotInstanceException(Throwable cause) {
		super(cause);
	}

}
