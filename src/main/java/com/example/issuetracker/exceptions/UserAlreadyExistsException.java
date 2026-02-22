package com.example.issuetracker.exceptions;

public class UserAlreadyExistsException extends BaseException{

    public UserAlreadyExistsException(String message){
        super(message);
    }

}
