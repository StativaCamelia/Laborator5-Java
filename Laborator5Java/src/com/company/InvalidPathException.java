package com.company;

public class InvalidPathException extends Exception{
    public InvalidPathException(Exception e){
        super("Invalid file", e);
    }
}
