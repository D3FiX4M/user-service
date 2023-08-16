package ru.microservices.user_service.core.exception;

import lombok.Getter;

@Getter
public class ExtendedException extends RuntimeException {
    private final ExtendedError error;


    public ExtendedException(ExtendedError error) {
        super();
        this.error = error;
    }

    public ExtendedException(String message, ExtendedError error) {
        super(message);
        this.error = error;
    }

    public static ExtendedException of(ExtendedError error) {
        return new ExtendedException(error);
    }

    public static ExtendedException of(Throwable cause) {
        return cause instanceof ExtendedException
                ? (ExtendedException) cause
                : new ExtendedException(
                cause.getMessage(),
                ExtendedError.INTERNAL
        );
    }
}
