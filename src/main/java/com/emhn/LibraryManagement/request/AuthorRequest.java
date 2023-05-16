package com.emhn.LibraryManagement.request;

import javax.validation.constraints.Pattern;

public class AuthorRequest {

  @Pattern(regexp = "[A-Za-z.\\s]+", message = "Author name must not be null or contain numbers")
  private String authorName;

  public AuthorRequest() {
  }

  public AuthorRequest(String authorName) {
    this.authorName = authorName;
  }

  public String getAuthorName() {
    return authorName;
  }
}
