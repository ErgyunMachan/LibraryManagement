package com.emhn.LibraryManagement.service;

import com.emhn.LibraryManagement.dto.AuthorDto;
import com.emhn.LibraryManagement.model.Author;
import com.emhn.LibraryManagement.request.AuthorRequest;

import java.util.List;

public interface AuthorService {

  List<AuthorDto> getAllAuthors();

  Author addAuthor(AuthorRequest authorRequest);

  Author removeAuthor(int id);

  AuthorDto getAuthorByID(int id);
}
