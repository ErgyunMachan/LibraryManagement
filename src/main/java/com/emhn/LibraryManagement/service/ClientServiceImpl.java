package com.emhn.LibraryManagement.service;

import com.emhn.LibraryManagement.dto.ClientDto;
import com.emhn.LibraryManagement.exception.AuthorNotFoundException;
import com.emhn.LibraryManagement.exception.ClientNotFoundException;
import com.emhn.LibraryManagement.mapper.ClientMapper;
import com.emhn.LibraryManagement.model.Client;
import com.emhn.LibraryManagement.repository.ClientRepository;
import com.emhn.LibraryManagement.request.ClientRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {

  private final ClientRepository clientRepository;

  private final ClientMapper clientMapper;

  public ClientServiceImpl(ClientRepository clientRepository, ClientMapper clientMapper) {
    this.clientRepository = clientRepository;
    this.clientMapper = clientMapper;
  }

  @Override
  public List<ClientDto> getAllClients() {
    List<Client> clientList = clientRepository.findAll();

    return clientMapper.mapclientsToDtosList(clientList);
  }

  @Override
  public ClientDto getClientByID(int id) {
    Optional<Client> findClient = clientRepository.findById(id);
    if (findClient.isEmpty()) {
      throw new ClientNotFoundException("Client is not exist with id: " + id);
    } else {
      return clientMapper.mapClientToDto(findClient.get());
    }
  }

  @Override
  public Client addClient(ClientRequest clientRequest) {
    Client client = new Client(clientRequest.getClientName());
    return clientRepository.save(client);
  }

  @Override
  public Client removeClient(int id) {
    Client clientToRemove;
    Optional<Client> client = clientRepository.findById(id);
    if (client.isPresent()) {
      clientToRemove = client.get();
    } else {
      throw new AuthorNotFoundException("Author not found");
    }

    clientRepository.delete(clientToRemove);

    return clientToRemove;
  }
}
