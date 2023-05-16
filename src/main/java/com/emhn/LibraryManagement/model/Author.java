package com.emhn.LibraryManagement.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "authors")
public class Author {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "author_id")
  private int authorID;

  @Column(name = "author_name")
  private String authorName;

  public Author() {
  }

  public Author(int authorID, String authorName) {
    this.authorID = authorID;
    this.authorName = authorName;
  }

  public Author(String authorName) {
    this.authorName = authorName;
  }

  public int getAuthorID() {
    return authorID;
  }

  public void setAuthorID(int authorID) {
    this.authorID = authorID;
  }

  public String getAuthorName() {
    return authorName;
  }

  public void setAuthorName(String authorName) {
    this.authorName = authorName;
  }
}
