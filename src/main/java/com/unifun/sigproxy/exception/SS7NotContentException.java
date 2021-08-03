package com.unifun.sigproxy.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NO_CONTENT)
public class SS7NotContentException extends RuntimeException{
    public SS7NotContentException() {
        super();
    }
    public SS7NotContentException(String message, Throwable cause) {
        super(message, cause);
    }
    public SS7NotContentException(String message) {
        super(message);
    }
    public SS7NotContentException(Throwable cause) {
        super(cause);
    }
}
