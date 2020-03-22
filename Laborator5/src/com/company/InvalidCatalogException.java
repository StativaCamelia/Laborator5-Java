package com.company;

public class InvalidCatalogException extends RuntimeException{
    public InvalidCatalogException(Exception e){
        super("Invalid catalog file", e);
    }
}
