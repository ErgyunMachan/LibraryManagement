package com.emhn.LibraryManagement.request;

import javax.validation.constraints.Pattern;

public class ClientRequest {

  @Pattern(regexp = "[A-Za-z\\s]+", message = "Author name must not be null or contain numbers")
  private String clientName;

  public ClientRequest() {
  }

  public ClientRequest(String clientName) {
    this.clientName = clientName;
  }

  public String getClientName() {
    return clientName;
  }
}
