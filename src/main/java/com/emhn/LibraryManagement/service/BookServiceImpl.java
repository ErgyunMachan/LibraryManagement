package com.emhn.LibraryManagement.service;

import com.emhn.LibraryManagement.dto.AuthorDto;
import com.emhn.LibraryManagement.dto.BookDto;
import com.emhn.LibraryManagement.exception.BookNotFoundException;
import com.emhn.LibraryManagement.mapper.BookMapper;
import com.emhn.LibraryManagement.mapper.DateMapper;
import com.emhn.LibraryManagement.model.Book;
import com.emhn.LibraryManagement.repository.BookRepository;
import com.emhn.LibraryManagement.request.BookRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

  private final BookRepository bookRepository;
  private final AuthorService authorService;
  private final DateMapper datemapper;

  private final BookMapper bookMapper;

  @Autowired
  public BookServiceImpl(
    BookRepository bookRepository, AuthorService authorService, DateMapper datemapper, BookMapper bookMapper) {
    this.bookRepository = bookRepository;
    this.authorService = authorService;
    this.datemapper = datemapper;
    this.bookMapper = bookMapper;
  }

  @Override
  public Book addBook(BookRequest bookRequest) {
    AuthorDto author = authorService.getAuthorByID(bookRequest.getAuthorID());
    LocalDate publishedDate = datemapper.mapStringToLocalDate(bookRequest.getPublishedDate());
    Book book = new Book(bookRequest.getBookName(), author.getAuthorName(), publishedDate, bookRequest.getQuantity());

    return bookRepository.save(book);
  }

  @Override
  public Book removeBook(int id) {
    Book bookToRemove;
    Optional<Book> book = bookRepository.findById(id);
    if (book.isPresent()) {
      bookToRemove = book.get();
    } else {
      throw new BookNotFoundException("Book is not found!!");
    }
    bookRepository.delete(bookToRemove);

    return bookToRemove;
  }

  @Override
  public BookDto getBookById(int id) {
    Book book;
    Optional<Book> findBook = bookRepository.findById(id);
    if (findBook.isPresent()) {
      book = findBook.get();
    } else {
      throw new BookNotFoundException("Book is not found!!");
    }
    return bookMapper.mapBookToDto(book);
  }

  @Override
  public List<BookDto> getAllBooks() {
    List<Book> books = bookRepository.findAll();

    return bookMapper.mapBooksToDtosList(books);
  }

  @Override
  public BookDto updateBook(BookRequest bookRequest, int bookID) {
    AuthorDto author = authorService.getAuthorByID(bookRequest.getAuthorID());
    Book updateBook;
    Optional<Book> optionalBook = bookRepository.findById(bookID);

    if (optionalBook.isPresent()) {
      updateBook = optionalBook.get();
    } else {
      throw new BookNotFoundException("Book is not found!!");
    }

    Book oldBook = new Book(updateBook.getBookName(), updateBook.getAuthorName(), updateBook.getPublishedDate(),
                            updateBook.getQuantity());

    updateBook.setBookName(bookRequest.getBookName());
    updateBook.setAuthorName(author.getAuthorName());
    updateBook.setPublishedDate(datemapper.mapStringToLocalDate(bookRequest.getPublishedDate()));
    updateBook.setQuantity(bookRequest.getQuantity());

    bookRepository.save(updateBook);

    return bookMapper.mapBookToDto(oldBook);
  }

  public Book updateQuantity(Book book) {
    return bookRepository.save(book);
  }

  @Override
  public List<BookDto> getAllAvailableBooks() {
    List<Book> booksList = bookRepository.findAll();
    List<BookDto> bookDtoList = bookMapper.mapBooksToDtosList(booksList);
    return bookDtoList.stream().filter(book -> book.getQuantity() != 0)
                      .collect(Collectors.toList());
  }

  @Override
  public List<BookDto> getBooksByAuthorID(int authorID) {
    AuthorDto author = authorService.getAuthorByID(authorID);

    return searchBooksByAuthor(author);
  }

  @Override
  public List<BookDto> searchBooksByAuthor(AuthorDto author) {
    List<Book> books = bookRepository.findAll();
    List<Book> filterBooks = books.stream().filter(book ->
                                                     book.getAuthorName().equals(author.getAuthorName()) &&
                                                     book.getQuantity() != 0).toList();
    if (books.isEmpty()) {
      throw new BookNotFoundException("Book is not found!!");
    } else {
      return bookMapper.mapBooksToDtosList(filterBooks);
    }
  }
}
