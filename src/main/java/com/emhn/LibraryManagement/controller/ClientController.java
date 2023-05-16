package com.emhn.LibraryManagement.controller;

import com.emhn.LibraryManagement.dto.ClientDto;
import com.emhn.LibraryManagement.model.Client;
import com.emhn.LibraryManagement.request.ClientRequest;
import com.emhn.LibraryManagement.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientController {

  private final ClientService clientService;

  @Autowired
  public ClientController(ClientService clientService) {
    this.clientService = clientService;
  }

  @PostMapping
  public ResponseEntity<Void> createClient(@RequestBody @Valid ClientRequest clientRequest) {
    Client client = clientService.addClient(clientRequest);

    URI location = UriComponentsBuilder.fromUriString("/clients/{id}")
                                       .buildAndExpand(client.getClientID())
                                       .toUri();
    return ResponseEntity.created(location).build();
  }

  @GetMapping
  public ResponseEntity<List<ClientDto>> getAllClients() {
    List<ClientDto> clientDtos = clientService.getAllClients();

    return ResponseEntity.ok(clientDtos);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ClientDto> getClientByID(@PathVariable int id) {
    ClientDto clientDto = clientService.getClientByID(id);

    return ResponseEntity.ok(clientDto);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Client> deleteAuthor(@PathVariable int id) {
    Client client = clientService.removeClient(id);

    return ResponseEntity.ok(client);
  }
}
