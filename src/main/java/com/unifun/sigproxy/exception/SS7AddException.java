package com.unifun.sigproxy.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class SS7AddException extends RuntimeException {
    public SS7AddException() {
        super();
    }

    public SS7AddException(String message, Throwable cause) {
        super(message, cause);
    }

    public SS7AddException(String message) {
        super(message);
    }

    public SS7AddException(Throwable cause) {
        super(cause);
    }
}
