package com.arc.db.jsd;

public class JsdException extends RuntimeException {
    public JsdException() {
        super();
    }

    public JsdException(String message) {
        super(message);
    }

    public JsdException(Throwable cause) {
        super(cause);
    }

    public JsdException(String message, Throwable cause) {
        super(message, cause);
    }
}
