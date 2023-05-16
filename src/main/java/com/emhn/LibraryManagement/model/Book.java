package com.emhn.LibraryManagement.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "books")
public class Book {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "book_id")
  private int bookID;
  @Column(name = "book_name")
  private String bookName;
  @Column(name = "author_name")
  private String authorName;
  @Column(name = "published_date")
  private LocalDate publishedDate;
  @Column(name = "quantity")
  private int quantity;

  public Book() {
  }

  public Book(String bookName, String authorName, LocalDate publishedDate, int quantity) {
    this.bookName = bookName;
    this.authorName = authorName;
    this.publishedDate = publishedDate;
    this.quantity = quantity;
  }

  public Book(int bookID, String bookName, String authorName, LocalDate publishedDate, int quantity) {
    this.bookID = bookID;
    this.bookName = bookName;
    this.authorName = authorName;
    this.publishedDate = publishedDate;
    this.quantity = quantity;
  }

  public int getBookID() {
    return bookID;
  }

  public void setBookID(int bookID) {
    this.bookID = bookID;
  }

  public String getBookName() {
    return bookName;
  }

  public void setBookName(String bookName) {
    this.bookName = bookName;
  }

  public String getAuthorName() {
    return authorName;
  }

  public void setAuthorName(String authorName) {
    this.authorName = authorName;
  }

  public LocalDate getPublishedDate() {
    return publishedDate;
  }

  public void setPublishedDate(LocalDate publishedDate) {
    this.publishedDate = publishedDate;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }
}
