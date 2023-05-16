package com.emhn.LibraryManagement.mapper;

import com.emhn.LibraryManagement.dto.ClientDto;
import com.emhn.LibraryManagement.model.Client;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ClientMapper {

  public List<ClientDto> mapclientsToDtosList(List<Client> clients) {
    List<ClientDto> clientDtos = new ArrayList<>();
    for (Client client : clients) {
      ClientDto clientDto = new ClientDto(client.getClientName());
      clientDtos.add(clientDto);
    }

    return clientDtos;
  }

  public ClientDto mapClientToDto(Client client) {

    return new ClientDto(client.getClientName());
  }
}
