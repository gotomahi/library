package com.library.library.exception;

public class BookNotAvailableException extends RuntimeException{

    public BookNotAvailableException(String message) {
        super(message);
    }
}
