package com.emhn.LibraryManagement.dto;

import java.time.LocalDate;

public class BookDto {

  private String bookName;
  private String authorName;
  private LocalDate publishedDate;
  private int quantity;

  public BookDto() {
  }

  public BookDto(String bookName, String authorName, LocalDate publishedDate, int quantity) {
    this.bookName = bookName;
    this.authorName = authorName;
    this.publishedDate = publishedDate;
    this.quantity = quantity;
  }

  public String getBookName() {
    return bookName;
  }

  public String getAuthorName() {
    return authorName;
  }

  public LocalDate getPublishedDate() {
    return publishedDate;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }
}
