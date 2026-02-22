package com.example.issuetracker.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundBaseException extends RuntimeException{

    public NotFoundBaseException(String message){
        super(message);
    }
}
