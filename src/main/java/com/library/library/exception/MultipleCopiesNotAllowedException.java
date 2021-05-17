package com.library.library.exception;

public class MultipleCopiesNotAllowedException extends RuntimeException{

    public MultipleCopiesNotAllowedException(String message) {
        super(message);
    }
}
