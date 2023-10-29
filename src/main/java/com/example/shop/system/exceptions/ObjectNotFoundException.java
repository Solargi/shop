package com.example.shop.system.exceptions;

public class ObjectNotFoundException extends RuntimeException {
    public ObjectNotFoundException (String objectName, Object objectId){
        super("could not find " + objectName + " with id " + objectId);
    }

}
