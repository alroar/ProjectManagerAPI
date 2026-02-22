package com.example.issuetracker.exceptions;

public class ProjectNotFoundException extends NotFoundBaseException{
    public ProjectNotFoundException(String message){
        super(message);
    }
}
