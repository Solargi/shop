package com.example.shop.system.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ExceptionHandlerAdvice {
   @ExceptionHandler(ObjectNotFoundException.class)
   ResponseEntity<Object> handlerObjectNotFoundException (ObjectNotFoundException ex){
      return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.NOT_FOUND);
   }

   @ExceptionHandler(MethodArgumentNotValidException.class)
   @ResponseStatus(HttpStatus.BAD_REQUEST)
   ResponseEntity<Object> handlerValidationException(MethodArgumentNotValidException ex){
      List<ObjectError> errors = ex.getBindingResult().getAllErrors();
      Map<String,String> map = new HashMap<>(errors.size());
      errors.forEach((error)->{
         String key = ((FieldError) error).getField();
         String val = error.getDefaultMessage();
         map.put(key, val);
      });
      return new ResponseEntity<Object>(map ,HttpStatus.BAD_REQUEST);
   }

   @ExceptionHandler(GenericException.class)
   ResponseEntity<Object> handlerGenericException (GenericException ex){
      return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.PRECONDITION_FAILED);
   }

}

