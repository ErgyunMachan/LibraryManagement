package com.emhn.LibraryManagement.exception;

public class OrderCreationNotAllowedException extends RuntimeException {

  public OrderCreationNotAllowedException(String message) {
    super(message);
  }
}
