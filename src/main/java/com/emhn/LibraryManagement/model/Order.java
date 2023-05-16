package com.emhn.LibraryManagement.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "orders")
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "order_id")
  private int orderID;
  @Column(name = "client_name")
  private String clientName;
  @Column(name = "book_name")
  private String bookName;
  @Column(name = "issue_date")
  private LocalDate issueDate;
  @Column(name = "due_date")
  private LocalDate dueDate;

  public Order() {
  }

  public Order(String clientName, String bookName, LocalDate issueDate, LocalDate dueDate) {
    this.clientName = clientName;
    this.bookName = bookName;
    this.issueDate = issueDate;
    this.dueDate = dueDate;
  }

  public Order(int orderID, String clientName, String bookName, LocalDate issueDate, LocalDate dueDate) {
    this.orderID = orderID;
    this.clientName = clientName;
    this.bookName = bookName;
    this.issueDate = issueDate;
    this.dueDate = dueDate;
  }

  public int getOrderID() {
    return orderID;
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
