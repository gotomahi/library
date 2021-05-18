package com.library.library.controller;

import com.library.library.model.GenericResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalControllerAdvice {
    @ExceptionHandler(value = {AccessDeniedException.class})
    public ResponseEntity<GenericResult> handleAccessDeniedException(Exception ex){
        GenericResult result = new GenericResult(false);
        result.addError("Unauthorized access");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
    }
}
