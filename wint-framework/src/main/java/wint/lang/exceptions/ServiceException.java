package wint.lang.exceptions;

import wint.lang.WintException;

public class ServiceException extends WintException {

	private static final long serialVersionUID = 7168345260665278583L;

	public ServiceException() {
		super();
	}

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServiceException(String message) {
		super(message);
	}

	public ServiceException(Throwable cause) {
		super(cause);
	}

}
