package com.emhn.LibraryManagement.controller;

import com.emhn.LibraryManagement.dto.BookDto;
import com.emhn.LibraryManagement.mapper.DateMapper;
import com.emhn.LibraryManagement.model.Book;
import com.emhn.LibraryManagement.request.BookRequest;
import com.emhn.LibraryManagement.service.BookService;
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

import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(BookController.class)
public class BookControllerTest {

  @Mock
  BookService bookService;

  @Mock
  private MockMvc mvc;


  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    mvc = MockMvcBuilders
      .standaloneSetup(new BookController(bookService))
      .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
      .build();
  }

  @Test
  public void testCreateBook() throws Exception {
    LocalDate publishedDate = LocalDate.of(2010, 5, 19);

    when(bookService.addBook(any())).thenReturn(
      new Book(1, "harry potter", "J. K. Rowling", publishedDate, 5));
    String json =
      "{\"bookName\" : \"harry potter\", \"authorID\" : 1 ,\"publishedDate\" : \"19/05/2010\", \"quantity\" : 5 }";


    mvc.perform(MockMvcRequestBuilders
                  .post("/books")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(json))
       .andExpect(status().isCreated())
       .andExpect(MockMvcResultMatchers.header().exists("Location"))
       .andExpect(MockMvcResultMatchers.header().string("Location", "/books/1"));

    verify(bookService).addBook(any(BookRequest.class));
  }

  @Test
  public void testGetAllBooks() throws Exception {
    LocalDate publishedDate = LocalDate.of(2010, 5, 19);

    when(bookService.getAllBooks()).thenReturn(
      Collections.singletonList(new BookDto("harry potter", "J. K. Rowling", publishedDate, 5)));
    mvc.perform(MockMvcRequestBuilders.get("/books"))
       .andExpect(status().isOk())
       .andExpect(MockMvcResultMatchers.jsonPath("$[*]").exists())
       .andExpect(MockMvcResultMatchers.jsonPath("$[0].authorName").value("J. K. Rowling"));
  }

  @Test
  public void testGetBookByID() throws Exception {
    LocalDate publishedDate = LocalDate.of(2010, 5, 19);
    when(bookService.getBookById(1)).thenReturn(new BookDto("harry potter", "J. K. Rowling", publishedDate, 5));
    mvc.perform(MockMvcRequestBuilders.get("/books/{id}", 1))
       .andExpect(status().isOk())
       .andExpect(MockMvcResultMatchers.jsonPath("$.*").exists())
       .andExpect(MockMvcResultMatchers.jsonPath("$.authorName").value("J. K. Rowling"));
  }

  @Test
  public void testGetAllAvailableBooks() throws Exception {
    LocalDate publishedDate = LocalDate.of(2010, 05, 19);
    when(bookService.getAllAvailableBooks()).thenReturn(
      Collections.singletonList(new BookDto("harry potter", "J. K. Rowling", publishedDate, 5)));
    mvc.perform(MockMvcRequestBuilders.get("/books/Available"))
       .andExpect(status().isOk())
       .andExpect(MockMvcResultMatchers.jsonPath("$[*]").exists())
       .andExpect(MockMvcResultMatchers.jsonPath("$[0].authorName").value("J. K. Rowling"));
  }

  @Test
  public void testGetBooksByAuthorID() throws Exception {
    LocalDate publishedDate = LocalDate.of(2010, 5, 19);

    when(bookService.getBooksByAuthorID(1)).thenReturn(
      Collections.singletonList(new BookDto("harry potter", "J. K. Rowling", publishedDate, 5)));
    mvc.perform(MockMvcRequestBuilders.get("/books/filter")
                                      .param("authorID", "1"))
       .andExpect(status().isOk())
       .andExpect(MockMvcResultMatchers.jsonPath("$[*]").exists())
       .andExpect(MockMvcResultMatchers.jsonPath("$[0].authorName").value("J. K. Rowling"));
  }

  @Test
  public void testUpdateBook() throws Exception {
    LocalDate publishedDate = LocalDate.of(2010, 5, 19);
    BookRequest newBook = new BookRequest("never die", 1, "01/05/2005", 5);
    when(bookService.updateBook(any(), anyInt())).thenReturn(
      new BookDto("harry potter", "J. K. Rowling", publishedDate, 5));
    String json =
      "{\"bookName\" : \"never die\", \"authorID\" : 1 ,\"publishedDate\" : \"01/05/2005\", \"quantity\" : 5 }";
    mvc.perform(MockMvcRequestBuilders.put("/books/{id}", 1)
                                      .contentType(MediaType.APPLICATION_JSON)
                                      .content(json))
       .andExpect(status().isOk())
       .andExpect(MockMvcResultMatchers.jsonPath("$.*").exists())
       .andExpect(MockMvcResultMatchers.jsonPath("$.authorName").value("J. K. Rowling"));
  }

  @Test
  public void testDeleteBook() throws Exception {
    LocalDate publishedDate = LocalDate.of(2010, 5, 19);
    when(bookService.removeBook(1)).thenReturn(new Book(1, "harry potter", "J. K. Rowling", publishedDate, 5));
    mvc.perform(MockMvcRequestBuilders.delete("/books/{id}", 1))
       .andExpect(status().isOk())
       .andExpect(MockMvcResultMatchers.jsonPath("$.*").exists())
       .andExpect(MockMvcResultMatchers.jsonPath("$.authorName").value("J. K. Rowling"));
  }
}