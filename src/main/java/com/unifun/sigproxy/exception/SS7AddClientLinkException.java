package com.unifun.sigproxy.exception;

public class SS7AddClientLinkException extends RuntimeException {
    public SS7AddClientLinkException() {
        super();
    }

    public SS7AddClientLinkException(String message, Throwable cause) {
        super(message, cause);
    }

    public SS7AddClientLinkException(String message) {
        super(message);
    }

    public SS7AddClientLinkException(Throwable cause) {
        super(cause);
    }
}
