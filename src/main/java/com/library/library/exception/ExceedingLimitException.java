package com.library.library.exception;

public class ExceedingLimitException extends RuntimeException{

    public ExceedingLimitException(String message) {
        super(message);
    }
}
