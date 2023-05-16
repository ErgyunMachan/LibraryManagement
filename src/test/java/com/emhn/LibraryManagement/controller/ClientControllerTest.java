package com.emhn.LibraryManagement.controller;

import com.emhn.LibraryManagement.dto.ClientDto;
import com.emhn.LibraryManagement.model.Client;
import com.emhn.LibraryManagement.request.ClientRequest;
import com.emhn.LibraryManagement.service.ClientService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(ClientController.class)
public class ClientControllerTest {
  @Mock
  ClientService clientService;
  @Mock
  private MockMvc mvc;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    mvc = MockMvcBuilders
      .standaloneSetup(new ClientController(clientService))
      .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
      .build();
  }

  @Test
  public void testCreateClient() throws Exception{

    when(clientService.addClient(any())).thenReturn(
      new Client(1, "Bill Johnson"));

    mvc.perform(MockMvcRequestBuilders
                  .post("/clients")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content("{\"clientName\" : \"Bill Johnson\"}"))
       .andExpect(status().isCreated())
       .andExpect(MockMvcResultMatchers.header().exists("Location"))
       .andExpect(MockMvcResultMatchers.header().string("Location", "/clients/1"));

    verify(clientService).addClient(any(ClientRequest.class));

  }

  @Test
  public void testGetAllClients() throws Exception {
    when(clientService.getAllClients()).thenReturn(Collections.singletonList(new ClientDto("Bill Johnson")));
    mvc.perform(MockMvcRequestBuilders.get("/clients"))
       .andExpect(status().isOk())
       .andExpect(content().json("[{'clientName':'Bill Johnson'}]"));
  }

  @Test
  public void testGetClientByID() throws Exception{
    when(clientService.getClientByID(1)).thenReturn(new ClientDto("Marry John"));
    mvc.perform(MockMvcRequestBuilders.get("/clients/{id}", 1))
       .andExpect(status().isOk())
       .andExpect(content().json("{\"clientName\" : \"Marry John\"}"));
  }

  @Test
  public void testDeleteAuthor() throws Exception {
    when(clientService.removeClient(1)).thenReturn(new Client(1, "Julian Matt"));
    mvc.perform(MockMvcRequestBuilders.delete("/clients/{id}", 1))
       .andExpect(status().isOk())
       .andExpect(content().json("{\"clientID\" : 1,\"clientName\" : \"Julian Matt\"}"));
  }
}