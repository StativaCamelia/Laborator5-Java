package com.company;

public class InvalidCommand extends RuntimeException{
    InvalidCommand(String message){
        super(message);
    }
}
