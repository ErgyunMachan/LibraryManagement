package com.emhn.LibraryManagement.service;

import com.emhn.LibraryManagement.dto.AuthorDto;
import com.emhn.LibraryManagement.dto.BookDto;
import com.emhn.LibraryManagement.model.Book;
import com.emhn.LibraryManagement.request.BookRequest;

import java.util.List;

public interface BookService {

  Book addBook(BookRequest bookRequest);

  Book removeBook(int id);

  BookDto getBookById(int id);

  List<BookDto> getAllBooks();

  BookDto updateBook(BookRequest bookRequest, int bookID);

  List<BookDto> getAllAvailableBooks();

  List<BookDto> getBooksByAuthorID(int authorID);

  List<BookDto> searchBooksByAuthor(AuthorDto author);

  Book updateQuantity(Book book);
}
