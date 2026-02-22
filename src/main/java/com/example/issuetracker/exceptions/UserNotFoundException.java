package com.example.issuetracker.exceptions;

public class UserNotFoundException extends NotFoundBaseException{

    public UserNotFoundException(String message){
        super(message);
    }
}
