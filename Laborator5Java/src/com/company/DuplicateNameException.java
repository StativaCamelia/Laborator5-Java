package com.company;

public class DuplicateNameException extends Exception {
    public DuplicateNameException(){
        super("Nu pot exista doua documente cu acelasi nume");
    }
}
