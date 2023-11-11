package com.example.shop.system.exceptions;

public class ObjectAlreadyExistException extends RuntimeException{
    public ObjectAlreadyExistException (String objectName, Object objectId){
        super(objectName + " with id " + objectId + " already exists");
    }
}
