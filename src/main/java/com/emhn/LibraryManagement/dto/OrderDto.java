package com.emhn.LibraryManagement.dto;

import java.time.LocalDate;

public class OrderDto {

  private final String clientName;
  private final String bookName;
  private final LocalDate issueDate;
  private final LocalDate dueDate;

  public OrderDto(String clientName, String bookName, LocalDate issueDate, LocalDate dueDate) {
    this.clientName = clientName;
    this.bookName = bookName;
    this.issueDate = issueDate;
    this.dueDate = dueDate;
  }

  public String getClientName() {
    return clientName;
  }

  public String getBookName() {
    return bookName;
  }

  public LocalDate getIssueDate() {
    return issueDate;
  }

  public LocalDate getDueDate() {
    return dueDate;
  }
}
