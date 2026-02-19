package com.example.issuetracker.exceptions;

<<<<<<< HEAD
public class IssueNotFoundException extends RuntimeException{
=======
public class IssueNotFoundException extends Exception{
>>>>>>> abccc12 (Creación de los servicios y repositorios de Issue y Project)

    public IssueNotFoundException(String message){
        super(message);
    }
}
