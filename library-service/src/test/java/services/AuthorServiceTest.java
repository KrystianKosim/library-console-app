package services;

import com.rbinternational.repository.models.entity.AuthorEntity;
import com.rbinternational.repository.models.entity.BookEntity;
import com.rbinternational.repository.models.repository.AuthorRepository;
import com.rbinternational.repository.models.repository.BookRepository;
import com.rbinternational.service.AuthorService;
import com.rbinternational.utils.ValuesService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.web.servlet.MockMvc;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@TestExecutionListeners(MockitoTestExecutionListener.class)
@ContextConfiguration
@AutoConfigureMockMvc
public class AuthorServiceTest {

    @Autowired
    MockMvc mockMvc;

    @Mock
    private AuthorRepository authorRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private ValuesService mockValuesService;

    @InjectMocks
    private AuthorService authorService;

    private final List<AuthorEntity> authorEntityList = new LinkedList<>();
    private final List<BookEntity> bookEntityList = new LinkedList<>();

    @Test
    @DisplayName("Should add author")
    void shouldAddAuthor() {
        String name = "Jan";
        String surname = "Nowak";

        when(mockValuesService.checkStringValueToFind(any(String.class))).thenReturn(true);

        boolean result = authorService.addAuthor(name, surname);
        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("Shouldn't add author because name is incorrect")
    void shouldntAddAuthorWithGivenNameAndSurname() {
        String name = "";
        String surname = "Nowak";

        when(mockValuesService.checkStringValueToFind(name)).thenReturn(false);

        boolean result = authorService.addAuthor(name, surname);

        Assertions.assertFalse(result);
    }

    @Test
    @DisplayName("Shouldn't add author because surname is incorrect")
    void shouldntAddAuthorBecauseSurnameIsIncorrect() {
        String name = "Jan";
        String surname = "";

        when(mockValuesService.checkStringValueToFind(surname)).thenReturn(false);
        when(mockValuesService.checkStringValueToFind(name)).thenReturn(true);
        boolean result = authorService.addAuthor(name, surname);

        Assertions.assertFalse(result);
    }

    @Test
    @DisplayName("Shouldn't add author because given values are null")
    void shouldntAddAuthorBecauseGivenValuesAreNull() {
        String name = null;
        String surname = null;

        when(mockValuesService.checkStringValueToFind(any())).thenReturn(false);

        boolean result = authorService.addAuthor(name, surname);

        Assertions.assertFalse(result);
    }

    @Test
    @DisplayName("Should return all authors")
    void shouldReturnAllAuthors() {
        List<AuthorEntity> authors = authorEntityList;

        when(authorRepository.findAll()).thenReturn(authors);

        List<AuthorEntity> result = authorService.findAllAuthors();

        Assertions.assertEquals(authors, result);
    }

    @Test
    @DisplayName("Should return authors with given parameters")
    void shouldReturnAuthorsWithGivenParameters() {
        String id = "1";
        String name = "Name1";
        String surname = "surname1";

        when(mockValuesService.checkIdValue(id)).thenReturn(true);
        when(mockValuesService.checkStringValueToFind(name)).thenReturn(true);
        when(mockValuesService.checkStringValueToFind(surname)).thenReturn(true);

        List<AuthorEntity> authors = authorEntityList;
        List<AuthorEntity> expectedResult = List.of(authors.get(0));
        List<AuthorEntity> result = authorService.findAuthors(id, name, surname, authors);

        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    @DisplayName("Should return empty list because couldn't find any author with given parameters")
    void shouldReturnEmptyListBecauseCouldntFindAnyAuthorWithGivenParameters() {
        String id = "33";
        String name = "Name1";
        String surname = "surname1";

        when(mockValuesService.checkIdValue(id)).thenReturn(true);
        when(mockValuesService.checkStringValueToFind(name)).thenReturn(true);
        when(mockValuesService.checkStringValueToFind(surname)).thenReturn(true);

        List<AuthorEntity> authors = authorEntityList;
        List<AuthorEntity> expectedResult = List.of(authors.get(0));
        List<AuthorEntity> result = authorService.findAuthors(id, name, surname, authors);

        Assertions.assertNotEquals(expectedResult, result);
    }

    @Test
    @DisplayName("Should return all authors because all of given parameters are incorrect")
    void shouldReturnAllAuthorsBecauseAllOfGivenParametersAreIncorrect() {
        String id = null;
        String name = null;
        String surname = null;

        when(mockValuesService.checkIdValue(id)).thenReturn(false);
        when(mockValuesService.checkStringValueToFind(name)).thenReturn(false);
        when(mockValuesService.checkStringValueToFind(surname)).thenReturn(false);

        List<AuthorEntity> authors = authorEntityList;
        List<AuthorEntity> expectedResult = authors;
        List<AuthorEntity> result = authorService.findAuthors(id, name, surname, authors);

        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    @DisplayName("Should return the same author because both of given values are incorrect to edit")
    void shouldReturnTheSameAuthorBecauseBothOfGivenValuesAreIncorrectToEdit() {
        String name = null;
        String surname = "";

        when(mockValuesService.checkStringValueToFind(name)).thenReturn(false);
        when(mockValuesService.checkStringValueToFind(surname)).thenReturn(false);

        AuthorEntity authorToEdit = authorEntityList.get(0);
        AuthorEntity result = authorService.editAuthor(name, surname, authorToEdit);
        Assertions.assertEquals(authorToEdit, result);
    }

    @Test
    @DisplayName("Should edit author name")
    void shouldReturnAuthorName() {
        String name = "Nowe";
        String surname = "";

        when(mockValuesService.checkStringValueToFind(name)).thenReturn(true);
        when(mockValuesService.checkStringValueToFind(surname)).thenReturn(false);

        AuthorEntity authorToEdit = authorEntityList.get(0);
        AuthorEntity result = authorService.editAuthor(name, surname, authorToEdit);
        authorToEdit.setName(name);
        Assertions.assertEquals(authorToEdit, result);
    }

    @Test
    @DisplayName("Should edit author surname")
    void shouldEditAuthorSurname() {
        String name = "";
        String surname = "Nowe";

        when(mockValuesService.checkStringValueToFind(name)).thenReturn(false);
        when(mockValuesService.checkStringValueToFind(surname)).thenReturn(true);

        AuthorEntity authorToEdit = authorEntityList.get(0);
        AuthorEntity result = authorService.editAuthor(name, surname, authorToEdit);
        authorToEdit.setSurname(surname);
        Assertions.assertEquals(authorToEdit, result);
    }

    @Test
    @DisplayName("Should edit author name and surname")
    void shouldEditAuthorNameAndSurname() {
        String name = "Nowe";
        String surname = "Nowe nazwisko";

        when(mockValuesService.checkStringValueToFind(name)).thenReturn(true);
        when(mockValuesService.checkStringValueToFind(surname)).thenReturn(true);

        AuthorEntity authorToEdit = authorEntityList.get(0);
        AuthorEntity result = authorService.editAuthor(name, surname, authorToEdit);
        authorToEdit.setName(name);
        authorToEdit.setSurname(surname);
        Assertions.assertEquals(authorToEdit, result);
    }

    @Test
    @DisplayName("Should delete given author")
    void shouldDeleteGivenAuthor() {
        AuthorEntity author = authorEntityList.get(0);

        boolean result = authorService.deleteAuthor(author);

        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("Should return null because author haven't got any books")
    void shouldReturnNullBecauseAuthorHaventGotAnyBooks() {
        AuthorEntity author = authorEntityList.get(0);
        when(bookRepository.findBookEntitiesByAuthorId(author)).thenReturn(Optional.empty());

        List<BookEntity> result = authorService.isAuthorHaveBooks(author);

        Assertions.assertNull(result);
    }

    @Test
    @DisplayName("Should return list with books which author have")
    void shouldReturnListOfBooksWhichAuthorHave() {
        AuthorEntity author = authorEntityList.get(0);
        List<BookEntity> bookEntities = List.of(bookEntityList.get(0));
        when(bookRepository.findBookEntitiesByAuthorId(author)).thenReturn(Optional.of(bookEntities));

        List<BookEntity> result = authorService.isAuthorHaveBooks(author);

        Assertions.assertEquals(bookEntities, result);
    }

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        authorService = new AuthorService(authorRepository, bookRepository, mockValuesService);
    }

    @BeforeEach
    public void initAuthorsAndBooks() {
        AuthorEntity author1 = new AuthorEntity();
        author1.setName("Name1");
        author1.setSurname("surname1");
        author1.setId(1);
        AuthorEntity author2 = new AuthorEntity();
        author2.setName("Name2");
        author2.setSurname("surname2");
        author2.setId(2);
        authorEntityList.add(author1);
        authorEntityList.add(author2);

        BookEntity book1 = new BookEntity();
        book1.setAuthorId(authorEntityList.get(0));
        book1.setId(1);
        book1.setTitle("title1");
        book1.setQuantity(4);
        BookEntity book2 = new BookEntity();
        book2.setAuthorId(authorEntityList.get(1));
        book2.setId(2);
        book2.setTitle("title2");
        book2.setQuantity(2);
        bookEntityList.add(book1);
        bookEntityList.add(book2);
    }
}
