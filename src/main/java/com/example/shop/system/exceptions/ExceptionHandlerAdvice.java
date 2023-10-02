package com.example.shop.system.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerAdvice {
   @ExceptionHandler(ObjectNotFoundException.class)
   ResponseEntity<Object> handlerObjectNotFoundException (ObjectNotFoundException ex){
      return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.NOT_FOUND);
   }
}

