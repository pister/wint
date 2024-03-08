package wint.lang.exceptions;

import wint.lang.WintException;

/**
 * @author songlihuang
 * @date 2024/3/7 17:29
 */
public class MethodNotAllowedException extends WintException {
    private static final long serialVersionUID = 4661760827847870206L;

    public MethodNotAllowedException() {
    }

    public MethodNotAllowedException(String message, Throwable cause) {
        super(message, cause);
    }

    public MethodNotAllowedException(String message) {
        super(message);
    }

    public MethodNotAllowedException(Throwable cause) {
        super(cause);
    }
}
