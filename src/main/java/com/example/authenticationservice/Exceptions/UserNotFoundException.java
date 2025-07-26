package com.example.authenticationservice.Exceptions;

public class UserNotFoundException extends Exception {

    public UserNotFoundException() {
        super("User with provided Email Id does not exist. Please Sign Up");
    }
}
