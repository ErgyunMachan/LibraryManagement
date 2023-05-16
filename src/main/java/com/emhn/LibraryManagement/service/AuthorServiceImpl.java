package com.emhn.LibraryManagement.service;

import com.emhn.LibraryManagement.dto.AuthorDto;
import com.emhn.LibraryManagement.exception.AuthorNotFoundException;
import com.emhn.LibraryManagement.mapper.AuthorMapper;
import com.emhn.LibraryManagement.model.Author;
import com.emhn.LibraryManagement.repository.AuthorRepository;
import com.emhn.LibraryManagement.request.AuthorRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorServiceImpl implements AuthorService {

  private final AuthorRepository authorRepository;
  private final AuthorMapper authorMapper;

  @Autowired
  public AuthorServiceImpl(AuthorRepository authorRepository, AuthorMapper authorMapper) {
    this.authorRepository = authorRepository;
    this.authorMapper = authorMapper;
  }

  @Override
  public List<AuthorDto> getAllAuthors() {
    List<Author> authors = authorRepository.findAll();
    return authorMapper.mapAuthorsToDtosList(authors);
  }

  @Override
  public Author addAuthor(AuthorRequest authorRequest) {

    Author author = new Author(authorRequest.getAuthorName());
    return authorRepository.save(author);
  }

  @Override
  public Author removeAuthor(int id) {
    Author authorToRemove;

    Optional<Author> author = authorRepository.findById(id);
    if (author.isPresent()) {
      authorToRemove = author.get();
    } else {
      throw new AuthorNotFoundException("Author not found");
    }

    authorRepository.delete(authorToRemove);

    return authorToRemove;
  }

  @Override
  public AuthorDto getAuthorByID(int id) {
    Author author;

    Optional<Author> findAuthor = authorRepository.findById(id);
    if (findAuthor.isPresent()) {
      author = findAuthor.get();
    } else {
      throw new AuthorNotFoundException("Author not found.");
    }

    return authorMapper.mapAuthorToDto(author);
  }
}
