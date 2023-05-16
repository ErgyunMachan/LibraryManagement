package com.emhn.LibraryManagement.dto;

public class AuthorDto {

  private final String authorName;

  public AuthorDto(String authorName) {
    this.authorName = authorName;
  }

  public String getAuthorName() {
    return authorName;
  }
}
