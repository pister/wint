package wint.lang.exceptions;

public class CanNotFindTemplateException extends ResourceException {

	private static final long serialVersionUID = 2893599720619054079L;

	public CanNotFindTemplateException() {
	}

	public CanNotFindTemplateException(String message, Throwable cause) {
		super(message, cause);
	}

	public CanNotFindTemplateException(String message) {
		super(message);
	}

	public CanNotFindTemplateException(Throwable cause) {
		super(cause);
	}

}
