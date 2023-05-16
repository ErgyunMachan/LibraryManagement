package com.emhn.LibraryManagement.controller;

import com.emhn.LibraryManagement.dto.AuthorDto;
import com.emhn.LibraryManagement.model.Author;
import com.emhn.LibraryManagement.request.AuthorRequest;
import com.emhn.LibraryManagement.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/authors")
public class AuthorController {

  private final AuthorService authorService;

  @Autowired
  public AuthorController(AuthorService authorService) {
    this.authorService = authorService;
  }

  @PostMapping
  public ResponseEntity<Author> createAuthor(@Valid @RequestBody AuthorRequest authorRequest) {
    Author createdAuthor = authorService.addAuthor(authorRequest);

    URI location = UriComponentsBuilder.fromUriString("/authors/{id}")
                                       .buildAndExpand(createdAuthor.getAuthorID())
                                       .toUri();


    return ResponseEntity.created(location).build();
  }

  @GetMapping
  public ResponseEntity<List<AuthorDto>> getAllAuthors() {
    List<AuthorDto> authors = authorService.getAllAuthors();

    return ResponseEntity.ok(authors);
  }

  @GetMapping("/{id}")
  public ResponseEntity<AuthorDto> getAuthorByID(@PathVariable int id) {
    AuthorDto authorDto = authorService.getAuthorByID(id);

    return ResponseEntity.ok(authorDto);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Author> deleteAuthor(@PathVariable int id) {
    Author author = authorService.removeAuthor(id);

    return ResponseEntity.ok(author);
  }
}
