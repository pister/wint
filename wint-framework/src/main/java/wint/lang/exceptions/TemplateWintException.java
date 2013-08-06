package wint.lang.exceptions;

import wint.lang.WintException;

/**
 * @author pister
 * 2012-2-11 08:33:28
 */
public class TemplateWintException extends WintException {

	private static final long serialVersionUID = 4594887890801291671L;

	public TemplateWintException() {
	}

	public TemplateWintException(String message, Throwable cause) {
		super(message, cause);
	}

	public TemplateWintException(String message) {
		super(message);
	}

	public TemplateWintException(Throwable cause) {
		super(cause);
	}

}
