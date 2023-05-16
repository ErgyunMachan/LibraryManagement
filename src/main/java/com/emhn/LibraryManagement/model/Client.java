package com.emhn.LibraryManagement.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "clients")
public class Client {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "client_id")
  private int clientID;

  @Column(name = "client_name")
  private String clientName;

  public Client() {
  }

  public Client(int clientID, String clientName) {
    this.clientID = clientID;
    this.clientName = clientName;
  }

  public Client(String clientName) {
    this.clientName = clientName;
  }

  public int getClientID() {
    return clientID;
  }

  public String getClientName() {
    return clientName;
  }

  public void setClientName(String clientName) {
    this.clientName = clientName;
  }
}
