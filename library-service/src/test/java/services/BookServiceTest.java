package services;

import com.rbinternational.repository.models.entity.AuthorEntity;
import com.rbinternational.repository.models.entity.BookEntity;
import com.rbinternational.repository.models.repository.AuthorRepository;
import com.rbinternational.repository.models.repository.BookRepository;
import com.rbinternational.repository.models.repository.LoansRepository;
import com.rbinternational.service.BookService;
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
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@TestExecutionListeners(MockitoTestExecutionListener.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@ContextConfiguration
@AutoConfigureMockMvc
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;
    @Mock
    private AuthorRepository authorRepository;
    @Mock
    private ValuesService valuesService;
    @Mock
    private LoansRepository loansRepository;
    @InjectMocks
    private BookService bookService;

    private List<BookEntity> books = new LinkedList<>();
    private List<AuthorEntity> authors = new LinkedList<>();


    @Test
    @DisplayName("Should add book")
    void shouldAddBook() {
        //given
        String authorName = "name";
        String authorSurname = "surname";
        AuthorEntity author = authors.get(0);

        String title = "title";
        String quantity = "13";

        when(authorRepository.findAuthorEntitiesByNameAndSurname(any(String.class), any(String.class))).thenReturn(Optional.of(author));

        boolean result = bookService.addBook(title, quantity, authorName, authorSurname);

        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("Shouldn't add book because author doesn't exists")
    void shouldntAddBookBecauseAuthorDoesntExists() {
        String authorName = "name";
        String authorSurname = "surname";

        String title = "title";
        String quantity = "13";

        when(authorRepository.findAuthorEntitiesByNameAndSurname(any(String.class), any(String.class))).thenReturn(Optional.empty());

        boolean result = bookService.addBook(title, quantity, authorName, authorSurname);

        Assertions.assertFalse(result);
    }

    @Test
    @DisplayName("Should return edited book")
    void shouldReturnEditedBook() {
        BookEntity book = books.get(0);
        String quantity = "99";
        AuthorEntity author = book.getAuthorId();

        when(valuesService.checkIdValue(any(String.class))).thenReturn(false);
        when(valuesService.checkStringValueToFind(any(String.class))).thenReturn(false);
        when(valuesService.checkQuantityValue(any(String.class))).thenReturn(true);
        when(authorRepository.findAuthorEntitiesByNameAndSurname(any(String.class), any(String.class))).thenReturn(Optional.of(author));

        BookEntity result = bookService.editBook(book, null, null, null, quantity);
        Assertions.assertTrue(Integer.valueOf(quantity) == result.getQuantity());
    }

    @Test
    @DisplayName("Shouldn't edit book because all values are the same")
    void shouldntEditBookBecauseAllValuesAreTheSame() {
        BookEntity book = books.get(0);

        when(valuesService.checkIdValue(any(String.class))).thenReturn(false);
        when(valuesService.checkStringValueToFind(any(String.class))).thenReturn(false);
        when(valuesService.checkQuantityValue(any(String.class))).thenReturn(false);

        BookEntity result = bookService.editBook(book, null, null, null, null);
        Assertions.assertEquals(book, result);
    }

    @Test
    @DisplayName("Should return list of books with given parameters")
    void shouldReturnListOfBooksWithGivenParameters() {
        List<BookEntity> bookEntities = books;
        BookEntity book = bookEntities.get(0);
        int quantity = book.getQuantity();

        when(valuesService.checkIdValue(any(String.class))).thenReturn(false);
        when(valuesService.checkStringValueToFind(any(String.class))).thenReturn(false);
        when(valuesService.checkQuantityValue(any(String.class))).thenReturn(true);

        List<BookEntity> results = bookService.findBookByParameters(null, null, null, null, String.valueOf(quantity), bookEntities);

        Assertions.assertEquals(List.of(book), results);
    }

    @Test
    @DisplayName("Should return all books given as parameter because all filter values are empty")
    void shouldReturnAllBooksGivenAsParameterBecauseAllFilterValuesAreEmpty() {
        List<BookEntity> bookEntities = books;

        when(valuesService.checkIdValue(any(String.class))).thenReturn(false);
        when(valuesService.checkStringValueToFind(any(String.class))).thenReturn(false);
        when(valuesService.checkQuantityValue(any(String.class))).thenReturn(false);

        List<BookEntity> results = bookService.findBookByParameters(null, null, null, null, null, bookEntities);

        Assertions.assertEquals(bookEntities, results);
    }

    @Test
    @DisplayName("Should convert given list of books id to list of book entities")
    void shouldConvertGivenListOfBooksIdToListOfBookEntities() {
        BookEntity book = books.get(0);
        List<Integer> booksId = new LinkedList<>();
        booksId.add(book.getId());

        when(bookRepository.findAll()).thenReturn(List.of(book));

        List<BookEntity> result = bookService.findBooksWhichReaderHaveTooMuchTime(booksId);

        Assertions.assertEquals(List.of(book), result);
    }


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        bookService = new BookService(bookRepository, authorRepository, valuesService, loansRepository);
    }

    @BeforeEach
    public void initValues() {
        String authorName1 = "name1";
        String authorSurname1 = "surname1";
        AuthorEntity author1 = new AuthorEntity();
        author1.setSurname(authorSurname1);
        author1.setName(authorName1);
        author1.setId(1);
        authors.add(author1);

        String authorName2 = "name2";
        String authorSurname2 = "surname2";
        AuthorEntity author2 = new AuthorEntity();
        author2.setName(authorName2);
        author2.setSurname(authorSurname2);
        author2.setId(2);
        authors.add(author2);

        BookEntity book1 = new BookEntity();
        book1.setAuthorId(author1);
        book1.setTitle("tittle1");
        book1.setQuantityAvailable(20);
        book1.setId(1);
        book1.setQuantity(3);
        books.add(book1);
        BookEntity book2 = new BookEntity();
        book2.setAuthorId(author2);
        book2.setTitle("tittle2");
        book2.setQuantityAvailable(10);
        book2.setQuantity(2);
        book2.setId(2);
        books.add(book2);
    }
}
