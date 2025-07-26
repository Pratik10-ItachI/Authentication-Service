package com.example.authenticationservice.Exceptions;

public class UserAlreadyExistsException extends Exception {
    public UserAlreadyExistsException(String message) {
        super("User with Email : "+message+" already exists");
    }
}
