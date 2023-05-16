package com.emhn.LibraryManagement.controller;

import com.emhn.LibraryManagement.dto.AuthorDto;
import com.emhn.LibraryManagement.model.Author;
import com.emhn.LibraryManagement.request.AuthorRequest;
import com.emhn.LibraryManagement.service.AuthorService;
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
@WebMvcTest(AuthorController.class)
public class AuthorControllerTest {

 @Mock
 AuthorService authorService;
@Mock
 private MockMvc mvc;

 @Before
 public void setUp() {
  MockitoAnnotations.initMocks(this);
  mvc = MockMvcBuilders
    .standaloneSetup(new AuthorController(authorService))
    .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
    .build();
 }

 @Test
 public void testCreateAuthor() throws Exception {
  when(authorService.addAuthor(any())).thenReturn(
    new Author(1, "J. K. Rowling"));

  mvc.perform(MockMvcRequestBuilders
                .post("/authors")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"authorName\" : \"J. K. Rowling\"}"))
     .andExpect(status().isCreated())
     .andExpect(MockMvcResultMatchers.header().exists("Location"))
     .andExpect(MockMvcResultMatchers.header().string("Location", "/authors/1"));

  verify(authorService).addAuthor(any(AuthorRequest.class));
 }

 @Test
 public void testGetAllAuthors() throws Exception {
  when(authorService.getAllAuthors()).thenReturn(Collections.singletonList(new AuthorDto("J. K. Rowling")));
  mvc.perform(MockMvcRequestBuilders.get("/authors"))
     .andExpect(status().isOk())
     .andExpect(content().json("[{'authorName':'J. K. Rowling'}]"));
 }

 @Test
 public void testGetAuthorByID() throws Exception {
  when(authorService.getAuthorByID(1)).thenReturn(new AuthorDto("J. K. Rowling"));
  mvc.perform(MockMvcRequestBuilders.get("/authors/{id}", 1))
     .andExpect(status().isOk())
     .andExpect(content().json("{\"authorName\" : \"J. K. Rowling\"}"));
 }

 @Test
 public void testDeleteAuthor() throws Exception {
  when(authorService.removeAuthor(1)).thenReturn(new Author(1, "J. K. Rowling"));
  mvc.perform(MockMvcRequestBuilders.delete("/authors/{id}", 1))
     .andExpect(status().isOk())
     .andExpect(content().json("{\"authorID\" : 1,\"authorName\" : \"J. K. Rowling\"}"));
 }
}