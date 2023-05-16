package com.emhn.LibraryManagement.request;

import org.springframework.lang.NonNull;

public class OrderRequest {

  @NonNull
  private int clientID;
  @NonNull
  private int bookID;

  public OrderRequest() {
  }

  public OrderRequest(int clientID, int bookID) {
    this.clientID = clientID;
    this.bookID = bookID;
  }

  public int getClientID() {
    return clientID;
  }

  public int getBookID() {
    return bookID;
  }
}
