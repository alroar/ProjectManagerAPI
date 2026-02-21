package com.example.issuetracker.exceptions;

public class ProjectArchivedException extends RuntimeException{
    public ProjectArchivedException(String message){
        super(message);
    }
}
