package com.emhn.LibraryManagement.service;

import com.emhn.LibraryManagement.dto.ClientDto;
import com.emhn.LibraryManagement.exception.ClientNotFoundException;
import com.emhn.LibraryManagement.mapper.ClientMapper;
import com.emhn.LibraryManagement.model.Client;
import com.emhn.LibraryManagement.repository.ClientRepository;
import com.emhn.LibraryManagement.request.ClientRequest;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ClientServiceImplTest {
  @Rule
  public ExpectedException exception = ExpectedException.none();

  @Mock
  ClientRepository clientRepository;

  @Mock
  ClientMapper clientMapper;

  @InjectMocks
  ClientServiceImpl clientService;

  @Test
  public void testGetAllClients() {
    when(clientRepository.findAll()).thenReturn(Collections.singletonList(new Client(1, "Bill Ryan")));
    when(clientMapper.mapclientsToDtosList(anyList())).thenReturn(Collections.singletonList(new ClientDto("Bill Ryan")));
    List<ClientDto> clientDtoList = clientService.getAllClients();
    Assert.assertEquals("Bill Ryan", clientDtoList.get(0).getClientName());
  }

  @Test
  public void testGetClientByID() {
    Client client = new Client(1,"Bill Ryan");
    Optional<Client> optionalClient = Optional.of(client);
    when(clientRepository.findById(1)).thenReturn(optionalClient);
    when(clientMapper.mapClientToDto(client)).thenReturn(new ClientDto("Bill Ryan"));
    ClientDto clientDto = clientService.getClientByID(1);
    Assert.assertEquals(clientDto.getClientName(),clientDto.getClientName());

  }

  @Test
  public void testGetClientByID_throws_ClientNotFound_Exception(){
    exception.expect(ClientNotFoundException.class);
    exception.expectMessage("Client is not exist with id: " + 1);
    when(clientRepository.findById(1)).thenThrow(new ClientNotFoundException("Client is not exist with id: " + 1));
    clientService.getClientByID(1);
  }

  @Test
  public void testAddClient() {
    when(clientRepository.save(any())).thenReturn(new Client(1,"Marry Johnson"));
    Client client = clientService.addClient(new ClientRequest("Marry Johnson"));
    Assert.assertEquals("Marry Johnson",client.getClientName());
  }

  @Test
  public void removeClient() {
    Client client = new Client(1,"Marry Johnson");
    Optional<Client> optionalClient = Optional.of(client);
    when(clientRepository.findById(1)).thenReturn(optionalClient);
    Client removedClient = clientService.removeClient(1);
    Assert.assertSame(removedClient,client);
  }

  @Test
  public void testRemoveClient_throws_ClientNotFound_Exception(){
    exception.expect(ClientNotFoundException.class);
    exception.expectMessage("Client is not exist with id: " + 1);
    when(clientRepository.findById(anyInt())).thenThrow(new ClientNotFoundException("Client is not exist with id: " + 1));
    clientService.removeClient(1);
  }
}