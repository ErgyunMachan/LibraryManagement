package com.emhn.LibraryManagement.controller;

import com.emhn.LibraryManagement.dto.BookDto;
import com.emhn.LibraryManagement.model.Book;
import com.emhn.LibraryManagement.request.BookRequest;
import com.emhn.LibraryManagement.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

  private final BookService bookService;

  public BookController(BookService bookService) {
    this.bookService = bookService;
  }

  @PostMapping
  public ResponseEntity<Void> createBook(@RequestBody @Valid BookRequest bookRequest) {
    Book book = bookService.addBook(bookRequest);
    URI location = UriComponentsBuilder.fromUriString("/books/{id}")
                                       .buildAndExpand(book.getBookID())
                                       .toUri();

    return ResponseEntity.created(location).build();
  }

  @GetMapping
  public ResponseEntity<List<BookDto>> getAllBooks() {
    List<BookDto> bookDtoList = bookService.getAllBooks();

    return ResponseEntity.ok(bookDtoList);
  }

  @GetMapping("/{id}")
  public ResponseEntity<BookDto> getBookByID(@PathVariable int id) {
    BookDto bookDto = bookService.getBookById(id);

    return ResponseEntity.ok(bookDto);
  }

  @GetMapping("/Available")
  public ResponseEntity<List<BookDto>> getAllAvailableBooks() {

    List<BookDto> books = bookService.getAllAvailableBooks();

    return ResponseEntity.ok(books);
  }

  @GetMapping("/filter")
  public ResponseEntity<List<BookDto>> getBooksByAuthorID(@RequestParam int authorID) {
    List<BookDto> books = bookService.getBooksByAuthorID(authorID);

    return ResponseEntity.ok(books);
  }

  @PutMapping("/{id}")
  public ResponseEntity<BookDto> updateBook(@RequestBody @Valid BookRequest bookRequest, @PathVariable int id) {
    BookDto bookDto = bookService.updateBook(bookRequest, id);

    return ResponseEntity.ok(bookDto);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Book> deleteBook(@PathVariable int id) {
    Book book = bookService.removeBook(id);

    return ResponseEntity.ok(book);
  }
}
