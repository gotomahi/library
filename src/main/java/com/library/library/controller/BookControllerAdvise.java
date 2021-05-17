package com.library.library.controller;

import com.library.library.exception.BookNotFoundException;
import com.library.library.exception.InvalidInputException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class BookControllerAdvise {
    @ExceptionHandler(value = {InvalidInputException.class})
    public ResponseEntity handleInvalidInputException(Exception ex){
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(value = {BookNotFoundException.class})
    public ResponseEntity handleBookNotFoundException(BookNotFoundException ex){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }
}
