package com.emhn.LibraryManagement.exception;

public class AuthorNotFoundException extends RuntimeException {

  public AuthorNotFoundException(String message) {
    super(message);
  }
}
