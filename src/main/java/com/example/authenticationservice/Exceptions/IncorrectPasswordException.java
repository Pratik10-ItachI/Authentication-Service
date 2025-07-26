package com.example.authenticationservice.Exceptions;

public class IncorrectPasswordException extends Exception {
  public IncorrectPasswordException() {
    super("Incorrect Password");
  }
}
