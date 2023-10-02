package com.example.shop.system.exceptions;

public class ObjectNotFoundException extends RuntimeException {
    public ObjectNotFoundException (String objectName, Integer id){
        super("could not find " + objectName + " with id " + id);
    }



}
