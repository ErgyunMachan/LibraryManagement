package com.emhn.LibraryManagement.service;

import com.emhn.LibraryManagement.dto.AuthorDto;
import com.emhn.LibraryManagement.dto.BookDto;
import com.emhn.LibraryManagement.exception.AuthorNotFoundException;
import com.emhn.LibraryManagement.exception.BookNotFoundException;
import com.emhn.LibraryManagement.mapper.BookMapper;
import com.emhn.LibraryManagement.mapper.DateMapper;
import com.emhn.LibraryManagement.model.Book;
import com.emhn.LibraryManagement.repository.BookRepository;
import com.emhn.LibraryManagement.request.BookRequest;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BookServiceImplTest {
  @Rule
  public ExpectedException exception = ExpectedException.none();
  @Mock
  BookRepository bookRepository;
  @Mock
  AuthorService authorService;
  @Mock
  BookMapper bookMapper;
  @Mock
  DateMapper dateMapper;
  @InjectMocks
  BookServiceImpl bookService;

  @Test
  public void testAddBook() {
    when(authorService.getAuthorByID(1)).thenReturn(new AuthorDto("J. K. Rowling"));
    when(bookRepository.save(any())).thenReturn(new Book(1,"harry potter","J. K. Rowling",
                                                         LocalDate.of(1990,2,19),5));
    Book createdBook = bookService.addBook(new BookRequest("harry potter",1,"19/2/1990",5));
    Assert.assertEquals("harry potter",createdBook.getBookName());
  }
  @Test
  public void testRemoveBook() {
    Book book = new Book(1,"harry potter","J. K. Rowling",
                         LocalDate.of(1990,2,19),5);
    Optional<Book> optionalBook = Optional.of(book);
    when(bookRepository.findById(1)).thenReturn(optionalBook);
    Book removedBook = bookService.removeBook(1);
    Assert.assertSame(removedBook,book);
  }

  @Test
  public void testRemoveBook_Throws_BookNotFound_Exception(){
    exception.expect(BookNotFoundException.class);
    exception.expectMessage("Book is not found!!");
    when(bookRepository.findById(anyInt())).thenThrow(new BookNotFoundException("Book is not found!!"));
    bookService.removeBook(anyInt());
  }

  @Test
  public void getBookById() {
    Book book = new Book(1,"harry potter","J. K. Rowling",
             LocalDate.of(1990,2,19),5);
    Optional<Book> optionalBook = Optional.of(book);
    when(bookRepository.findById(1)).thenReturn(optionalBook);
    when(bookMapper.mapBookToDto(book)).thenReturn(new BookDto("harry potter","J. K. Rowling",
                                                               LocalDate.of(1990,2,19),5));
    BookDto bookDto = bookService.getBookById(1);
    Assert.assertEquals(book.getBookName(),bookDto.getBookName());
  }

  @Test
  public void testGetBookByID_throws_Book_NotFound_Exception(){
    exception.expect(BookNotFoundException.class);
    exception.expectMessage("Book is not found!!");
    when(bookRepository.findById(anyInt())).thenThrow(new BookNotFoundException("Book is not found!!"));
    bookService.getBookById(anyInt());
  }

  @Test
  public void testGetAllBooks() {

    when(bookRepository.findAll()).thenReturn(Collections.singletonList(new Book(1,"harry potter","J. K. Rowling",
                                                                                 LocalDate.of(1990,2,19),5)));
    when(bookMapper.mapBooksToDtosList(anyList())).thenReturn(Collections.singletonList(new BookDto("harry potter","J. K. Rowling",
                                                                                                    LocalDate.of(1990,2,19),5)));
    List<BookDto> bookDtoList = bookService.getAllBooks();
    Assert.assertEquals("harry potter", bookDtoList.get(0).getBookName());
  }

  @Test
  public void testUpdateBook() {
    BookRequest bookRequest = new BookRequest("harry potter",1,"19/2/1990",5);
    Book updatedBook = new Book(1,"harry potter","J. K. Rowling",
                         LocalDate.of(1990,2,19),5);
    Optional<Book> optionalBook = Optional.of(updatedBook);
    when(authorService.getAuthorByID(anyInt())).thenReturn(new AuthorDto("J. K. Rowling"));
    when(bookRepository.findById(anyInt())).thenReturn(optionalBook);
    Book oldBook = new Book(optionalBook.get().getBookName(),optionalBook.get().getAuthorName(),optionalBook.get().getPublishedDate(),optionalBook.get().getQuantity());
    updatedBook.setBookName("harry");
    updatedBook.setAuthorName("Rowling");
    when(bookMapper.mapBookToDto(any())).thenReturn(new BookDto("harry potter","J. K. Rowling",LocalDate.of(1990,2,19),5));
    bookRepository.save(updatedBook);
   BookDto returnedOldBook = bookService.updateBook(bookRequest,1);
    Assert.assertEquals("harry potter",returnedOldBook.getBookName());
  }

  @Test
  public void testUpdateBook_Throws_AuthorNotFound_Exception(){
    BookRequest bookRequest = new BookRequest("harry potter",1,"19/2/1990",5);
    exception.expect(AuthorNotFoundException.class);
    exception.expectMessage("Author not found.");
    when(authorService.getAuthorByID(anyInt())).thenThrow(new AuthorNotFoundException("Author not found."));
    bookService.updateBook(bookRequest,anyInt());
  }

  @Test
  public void testUpdateBook_Throws_BookNotFound_Exception(){
    BookRequest bookRequest = new BookRequest("harry potter",1,"19/2/1990",5);
    exception.expect(BookNotFoundException.class);
    exception.expectMessage("Book is not found!!");
    when(bookRepository.findById(anyInt())).thenThrow(new BookNotFoundException("Book is not found!!"));
    bookService.updateBook(bookRequest,anyInt());
  }



  @Test
  public void testUpdateQuantity() {
    Book existedBook = new Book(1,"harry potter","J. K. Rowling",
                                LocalDate.of(1990,2,19),5);
    Book bookToUpdate = new Book(1,"harry potter","J. K. Rowling",
                                LocalDate.of(1990,2,19),4);
    when(bookRepository.save(bookToUpdate)).thenReturn(new Book(1,"harry potter","J. K. Rowling",
                                                              LocalDate.of(1990,2,19),4));
    Book updatedBook = bookService.updateQuantity(bookToUpdate);
    Assert.assertEquals(4, updatedBook.getQuantity());
  }

  @Test
  public void getAllAvailableBooks() {
    List<BookDto> bookDtoList = new ArrayList<>();
    bookDtoList.add(new BookDto("harry potter","J. K. Rowling",
                                LocalDate.of(1990,2,19),4));
    when(bookRepository.findAll()).thenReturn(Collections.singletonList(new Book(1,"harry potter","J. K. Rowling",
                                                                                 LocalDate.of(1990,2,19),4) ));
    when(bookMapper.mapBooksToDtosList(anyList())).thenReturn(Collections.singletonList(new BookDto("harry potter","J. K. Rowling",
                                                                                                    LocalDate.of(1990,2,19),4)));
    List<BookDto> availableBooks = bookDtoList.stream().filter(book -> book.getQuantity() != 0).toList();
    List<BookDto> listOfAvailableBooks = bookService.getAllAvailableBooks();
    Assert.assertSame(availableBooks.get(0).getBookName(),listOfAvailableBooks.get(0).getBookName());
  }

  @Test
  public void testGetBooksByAuthorID() {
    when(authorService.getAuthorByID(1)).thenReturn(new AuthorDto("J. K. Rowling"));
    when(bookRepository.findAll()).thenReturn(Collections.singletonList(new Book(1,"harry potter","J. K. Rowling",
                                                                                 LocalDate.of(1990,2,19),5)));
    when(bookService.searchBooksByAuthor(new AuthorDto("J. K. Rowling"))).thenReturn(Collections.singletonList(new BookDto("harry potter","J. K. Rowling",
                                                                                                                       LocalDate.of(1990,2,19),5)));
    List<BookDto> booksByAuthorsList = bookService.getBooksByAuthorID(1);
    Assert.assertEquals(booksByAuthorsList.get(0).getBookName(),"harry potter");
  }

  @Test
  public void testSearchBooksByAuthor() {
    AuthorDto author = new AuthorDto("J. K. Rowling");
    when(bookRepository.findAll()).thenReturn(Collections.singletonList(new Book(1,"harry potter","J. K. Rowling",
                                                                                 LocalDate.of(1990,2,19),5)));
    when(bookMapper.mapBooksToDtosList(anyList())).thenReturn(
                                                          Collections.singletonList(new BookDto("harry potter","J. K. Rowling",
                                                                                                LocalDate.of(1990,2,19),5)));
    List<BookDto> filterBooksByAuthor = bookService.searchBooksByAuthor(author);
    Assert.assertEquals("J. K. Rowling",filterBooksByAuthor.get(0).getAuthorName());
  }
}