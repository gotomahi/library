package com.library.library.controller;

import com.library.library.model.GenericResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class GlobalControllerAdviceTest {
    @Autowired
    private GlobalControllerAdvice controllerAdvice;

    @Test
    public void test_access_denied_exception(){
        ResponseEntity<GenericResult> response = controllerAdvice.handleAccessDeniedException(new AccessDeniedException("Unauthorized"));
        assertEquals(401, response.getStatusCodeValue());
        assertEquals(true, response.getBody().hasError("Unauthorized access"));
    }
}
