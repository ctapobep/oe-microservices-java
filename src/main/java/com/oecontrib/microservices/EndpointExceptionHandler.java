package com.oecontrib.microservices;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice("com.oecontrib.microservices.v2")
public class EndpointExceptionHandler {
    @ExceptionHandler(WrongMoleculeFormatException.class)
    public ResponseEntity duplicateException(WrongMoleculeFormatException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

}
