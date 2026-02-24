package com.example.issuetracker.exceptions;

public class RoleNotFoundException extends RuntimeException{
    public RoleNotFoundException(String message){
        super(message);
    }
}
