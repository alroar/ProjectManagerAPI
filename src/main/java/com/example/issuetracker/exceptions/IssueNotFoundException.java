package com.example.issuetracker.exceptions;

public class IssueNotFoundException extends RuntimeException{

    public IssueNotFoundException(String message){
        super(message);
    }
}
