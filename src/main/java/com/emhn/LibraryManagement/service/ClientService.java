package com.emhn.LibraryManagement.service;

import com.emhn.LibraryManagement.dto.ClientDto;
import com.emhn.LibraryManagement.model.Client;
import com.emhn.LibraryManagement.request.ClientRequest;

import java.util.List;

public interface ClientService {

  List<ClientDto> getAllClients();

  ClientDto getClientByID(int id);

  Client addClient(ClientRequest clientRequest);

  Client removeClient(int id);
}
