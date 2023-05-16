package com.emhn.LibraryManagement.dto;

public class ClientDto {

  private final String clientName;

  public ClientDto(String clientName) {
    this.clientName = clientName;
  }

  public String getClientName() {
    return clientName;
  }
}
