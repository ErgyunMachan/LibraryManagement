package com.emhn.LibraryManagement.service;

import com.emhn.LibraryManagement.dto.AuthorDto;
import com.emhn.LibraryManagement.exception.AuthorNotFoundException;
import com.emhn.LibraryManagement.mapper.AuthorMapper;
import com.emhn.LibraryManagement.model.Author;
import com.emhn.LibraryManagement.repository.AuthorRepository;
import com.emhn.LibraryManagement.request.AuthorRequest;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuthorServiceImplTest {
  @Rule
  public ExpectedException exception = ExpectedException.none();

  @Mock
  AuthorRepository authorRepository;

  @Mock
  AuthorMapper authorMapper;

  @InjectMocks
  AuthorServiceImpl authorService;

  @Test
  public void testGetAllAuthors() {
    List<Author> authorList = List.of(new Author(1,"Jack Ryan"));
     when(authorRepository.findAll()).thenReturn(Collections.singletonList(new Author(1,"Jack Ryan")));
     when(authorMapper.mapAuthorsToDtosList(anyList())).thenReturn(Collections.singletonList(new AuthorDto("Jack Ryan")));
     List<AuthorDto> authorDtoList = authorService.getAllAuthors();
    Assert.assertEquals("Jack Ryan", authorDtoList.get(0).getAuthorName());
  }

  @Test
  public void testAddAuthor() {

    when(authorRepository.save(any())).thenReturn(new Author(1,"Hasan bas"));
    Author author = authorService.addAuthor(new AuthorRequest("Hasan bas"));
    Assert.assertEquals("Hasan bas",author.getAuthorName());
  }

  @Test
  public void testRemoveAuthor() {
    Author author = new Author(1,"Nani nelson");
    Optional<Author> optionalAuthor = Optional.of(author);
    when(authorRepository.findById(1)).thenReturn(optionalAuthor);
    Author removedAuthor = authorService.removeAuthor(1);
    Assert.assertSame(removedAuthor,author);

  }

  @Test
  public void testRemoveAuthor_throws_AuthorNotFound_Exception(){
    exception.expect(AuthorNotFoundException.class);
    exception.expectMessage("Author not found.");
    when(authorRepository.findById(anyInt())).thenThrow(new AuthorNotFoundException("Author not found."));
    authorService.removeAuthor(anyInt());
  }

  @Test
  public void testGetAuthorByID() {
    Author author = new Author(1,"Nani nelson");
    Optional<Author> optionalAuthor = Optional.of(author);
    when(authorRepository.findById(1)).thenReturn(optionalAuthor);
    when(authorMapper.mapAuthorToDto(author)).thenReturn(new AuthorDto("Nani nelson"));
    AuthorDto authorDto = authorService.getAuthorByID(1);
    Assert.assertEquals(authorDto.getAuthorName(),author.getAuthorName());
  }

  @Test
  public void testGetAuthorByID_throws_AuthorNotFound_Exception(){
    exception.expect(AuthorNotFoundException.class);
    exception.expectMessage("Author not found.");
    when(authorRepository.findById(anyInt())).thenThrow(new AuthorNotFoundException("Author not found."));
    authorService.getAuthorByID(anyInt());
  }
}