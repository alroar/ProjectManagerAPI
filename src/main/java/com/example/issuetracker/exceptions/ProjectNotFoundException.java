package com.example.issuetracker.exceptions;

<<<<<<< HEAD
public class ProjectNotFoundException extends RuntimeException{
=======
public class ProjectNotFoundException extends Exception{
>>>>>>> abccc12 (Creación de los servicios y repositorios de Issue y Project)

    public ProjectNotFoundException(String message){
        super(message);
    }

}
