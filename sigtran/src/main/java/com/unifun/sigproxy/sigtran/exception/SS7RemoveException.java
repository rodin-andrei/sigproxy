package com.unifun.sigproxy.sigtran.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class SS7RemoveException extends RuntimeException {
    public SS7RemoveException() {
        super();
    }

    public SS7RemoveException(String message, Throwable cause) {
        super(message, cause);
    }

    public SS7RemoveException(String message) {
        super(message);
    }

    public SS7RemoveException(Throwable cause) {
        super(cause);
    }
}
