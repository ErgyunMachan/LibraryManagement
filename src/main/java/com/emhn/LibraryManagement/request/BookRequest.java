package com.emhn.LibraryManagement.request;

import org.springframework.lang.NonNull;

import javax.validation.constraints.Pattern;

public class BookRequest {

  @Pattern(regexp = "[A-Za-z\\s]+", message = "Author name must not be null or contain numbers")
  private String bookName;
  @NonNull
  private int authorID;

  @Pattern(regexp = "(3[01]|[12][0-9]|0?[1-9])/(1[0-2]|0?[1-9])/[0-9]{2}[0-9]{2}")
  private String publishedDate;
  @NonNull
  private int quantity;

  public BookRequest() {
  }

  public BookRequest(int quantity) {
    this.quantity = quantity;
  }

  public BookRequest(String bookName, int authorID, String publishedDate, int quantity) {
    this.bookName = bookName;
    this.authorID = authorID;
    this.publishedDate = publishedDate;
    this.quantity = quantity;
  }

  public String getBookName() {
    return bookName;
  }

  public int getAuthorID() {
    return authorID;
  }

  public String getPublishedDate() {
    return publishedDate;
  }

  public int getQuantity() {
    return quantity;
  }
}
