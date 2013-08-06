package wint.lang.exceptions;

import wint.lang.WintException;

public class FlowDataException extends WintException {

	private static final long serialVersionUID = 4750326687210347012L;

	public FlowDataException() {
		super();
	}

	public FlowDataException(String message, Throwable cause) {
		super(message, cause);
	}

	public FlowDataException(String message) {
		super(message);
	}

	public FlowDataException(Throwable cause) {
		super(cause);
	}

}
