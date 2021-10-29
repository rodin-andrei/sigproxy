package com.unifun.sigproxy.sigtran.exception;

public class InitializingException extends Exception {
    public InitializingException(String message) {
        super(message);
    }

    public InitializingException(String message, Throwable cause) {
        super(message, cause);
    }
}
