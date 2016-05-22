package com.oecontrib.microservices;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice("com.oecontrib.microservices.v2")
public class EndpointExceptionHandler {
    @ExceptionHandler({WrongMoleculeFormatException.class, HttpMediaTypeNotAcceptableException.class})
    public ResponseEntity duplicateException(WrongMoleculeFormatException exception) {
        exception.printStackTrace();//need to add a logger at some point
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(exception.getMessage());
    }

}
