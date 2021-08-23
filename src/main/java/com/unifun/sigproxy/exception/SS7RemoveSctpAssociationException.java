package com.unifun.sigproxy.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class SS7RemoveSctpAssociationException extends RuntimeException {
    public SS7RemoveSctpAssociationException() {
        super();
    }

    public SS7RemoveSctpAssociationException(String message, Throwable cause) {
        super(message, cause);
    }

    public SS7RemoveSctpAssociationException(String message) {
        super(message);
    }

    public SS7RemoveSctpAssociationException(Throwable cause) {
        super(cause);
    }
}
