package com.pelgray.otus.jdbc.exception;

public class EntityClassMetaDataException extends RuntimeException {
    public EntityClassMetaDataException(String message) {
        super(message);
    }

    public EntityClassMetaDataException(Throwable cause) {
        super(cause);
    }
}
