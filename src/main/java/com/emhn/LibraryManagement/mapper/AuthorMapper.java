package com.emhn.LibraryManagement.mapper;

import com.emhn.LibraryManagement.dto.AuthorDto;
import com.emhn.LibraryManagement.model.Author;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AuthorMapper {

  public List<AuthorDto> mapAuthorsToDtosList(List<Author> authors) {
    List<AuthorDto> authorDtos = new ArrayList<>();
    for (Author author : authors) {
      AuthorDto authorDto = new AuthorDto(author.getAuthorName());
      authorDtos.add(authorDto);
    }

    return authorDtos;
  }

  public AuthorDto mapAuthorToDto(Author author) {

    return new AuthorDto(author.getAuthorName());
  }
}
