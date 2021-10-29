package com.unifun.sigproxy.sigtran.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class SS7NotFoundException extends RuntimeException {
    public SS7NotFoundException() {
        super();
    }

    public SS7NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public SS7NotFoundException(String message) {
        super(message);
    }

    public SS7NotFoundException(Throwable cause) {
        super(cause);
    }
}
