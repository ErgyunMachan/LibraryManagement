package com.emhn.LibraryManagement.mapper;

import com.emhn.LibraryManagement.dto.BookDto;
import com.emhn.LibraryManagement.model.Book;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BookMapper {

  public List<BookDto> mapBooksToDtosList(List<Book> books) {
    List<BookDto> bookDtos = new ArrayList<>();
    for (Book book : books) {
      BookDto bookDto =
        new BookDto(book.getBookName(), book.getAuthorName(), book.getPublishedDate(), book.getQuantity());
      bookDtos.add(bookDto);
    }

    return bookDtos;
  }

  public BookDto mapBookToDto(Book book) {

    return new BookDto(book.getBookName(), book.getAuthorName(), book.getPublishedDate(), book.getQuantity());
  }
}
