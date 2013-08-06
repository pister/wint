package wint.lang.exceptions;

public class ServiceInitialException extends ServiceException {

	private static final long serialVersionUID = 6801856439534654485L;

	public ServiceInitialException() {
		super();
	}

	public ServiceInitialException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServiceInitialException(String message) {
		super(message);
	}

	public ServiceInitialException(Throwable cause) {
		super(cause);
	}

}
