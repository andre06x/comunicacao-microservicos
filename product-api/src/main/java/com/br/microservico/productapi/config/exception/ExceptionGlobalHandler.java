package com.br.microservico.productapi.config.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionGlobalHandler {

    @ExceptionHandler(ValidationExcpetion.class)
    public ResponseEntity<?> handleValidationException(ValidationExcpetion validationExcpetion){
        var details = new ExceptionDetails();
        details.setStatus(HttpStatus.BAD_REQUEST.value());
        details.setMessage(validationExcpetion.getMessage());

        return new ResponseEntity<>(details, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthtenticationExcpetion.class)
    public ResponseEntity<?> handleAuthtenticationException(AuthtenticationExcpetion authtenticationException){
        var details = new ExceptionDetails();
        details.setStatus(HttpStatus.UNAUTHORIZED.value());
        details.setMessage(authtenticationException.getMessage());

        return new ResponseEntity<>(details, HttpStatus.UNAUTHORIZED);
    }
}
