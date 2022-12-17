package services;

import com.rbinternational.repository.models.entity.ReaderEntity;
import com.rbinternational.repository.models.repository.LoansRepository;
import com.rbinternational.repository.models.repository.ReaderRepository;
import com.rbinternational.service.ConfigurationService;
import com.rbinternational.service.LoanService;
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

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestExecutionListeners(MockitoTestExecutionListener.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@ContextConfiguration
@AutoConfigureMockMvc
public class LoanServiceTest {

    @Mock
    private LoansRepository loansRepository;
    @Mock
    private ReaderRepository readerRepository;
    @Mock
    private ConfigurationService configurationService;
    @InjectMocks
    private LoanService loanService;

    private List<ReaderEntity> readers = new LinkedList<>();

    @Test
    @DisplayName("Should set number of current borrow books for readers")
    void shouldSetNumberOfCurrentBorrowBooksForReaders() {
        ReaderEntity reader = readers.get(0);
        List<ReaderEntity> readers = List.of(reader);
        int numberOfBooks = 20;

        when(loansRepository.getNumberOfCurrentlyBorrowedBooksForReader(any(Integer.class))).thenReturn(numberOfBooks);
        when(readerRepository.findAll()).thenReturn(readers);
        List<ReaderEntity> result = loanService.setNumberOfBorrowedBooksReader();

        reader.setNumberOfCurrentlyBorrowedBooks(numberOfBooks);
        Assertions.assertEquals(readers, result);
    }

    @Test
    @DisplayName("Should set number of every borrowed books for reader")
    void shouldSetNumberOfEveryBorrowedBooksForReader() {
        ReaderEntity reader = readers.get(0);
        List<ReaderEntity> readers = List.of(reader);
        int numberOfEveryBorrowedBooks = 30;

        when(readerRepository.findAll()).thenReturn(readers);
        when(loansRepository.getNumberOfEveryBorrowedBooksForReader(any(Integer.class))).thenReturn(numberOfEveryBorrowedBooks);

        List<ReaderEntity> result = loanService.setNumberOfEveryBorrowedBooksReader();
        reader.setNumberOfEveryBorrowedBooks(numberOfEveryBorrowedBooks);

        Assertions.assertEquals(readers, result);
    }

    @Test
    @DisplayName("Should return true because given reader can loan a book")
    void shouldReturnTrueBecauseGivenReaderCanLoanABook() {
        ReaderEntity reader = readers.get(0);
        int numberOfCurrentlyBorrowBook = 2;
        int minAgeToBorrowABook = 15;
        int maxNumberOfBorrowBook = 3;

        when(loansRepository.getNumberOfCurrentlyBorrowedBooksForReader(any(Integer.class))).thenReturn(numberOfCurrentlyBorrowBook);
        when(configurationService.getMinAgeToBorrowABook()).thenReturn(minAgeToBorrowABook);
        when(configurationService.getMaxNumberOfBorrowedBooks()).thenReturn(maxNumberOfBorrowBook);

        boolean result = loanService.isReaderCanLoanBook(reader);

        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("Shouldn't loan a book because reader is too young")
    void shouldntLoanABookBecauseReaderIsTooYoung() {
        ReaderEntity reader = readers.get(0);
        int numberOfCurrentlyBorrowBook = 2;
        int minAgeToBorrowABook = 23;
        int maxNumberOfBorrowBook = 3;

        when(loansRepository.getNumberOfCurrentlyBorrowedBooksForReader(any(Integer.class))).thenReturn(numberOfCurrentlyBorrowBook);
        when(configurationService.getMinAgeToBorrowABook()).thenReturn(minAgeToBorrowABook);
        when(configurationService.getMaxNumberOfBorrowedBooks()).thenReturn(maxNumberOfBorrowBook);

        boolean result = loanService.isReaderCanLoanBook(reader);

        Assertions.assertFalse(result);
    }

    @Test
    @DisplayName("Shouldn't loan a book because reader have too much books")
    void shouldntLoanABookBecauseReaderHaveTooMuchBooks() {
        ReaderEntity reader = readers.get(0);

        int numberOfCurrentlyBorrowBook = 3;
        int minAgeToBorrowABook = 15;
        int maxNumberOfBorrowBook = 3;

        when(loansRepository.getNumberOfCurrentlyBorrowedBooksForReader(any(Integer.class))).thenReturn(numberOfCurrentlyBorrowBook);

        when(configurationService.getMinAgeToBorrowABook()).thenReturn(minAgeToBorrowABook);
        when(configurationService.getMaxNumberOfBorrowedBooks()).thenReturn(maxNumberOfBorrowBook);

        boolean result = loanService.isReaderCanLoanBook(reader);

        Assertions.assertFalse(result);
    }

    @BeforeEach
    public void initValues() {
        ReaderEntity reader1 = new ReaderEntity();
        reader1.setId(1);
        reader1.setName("Jan");
        reader1.setSurname("Nowak");
        reader1.setBirthDate(LocalDate.now().minusYears(20));

        ReaderEntity reader2 = new ReaderEntity();
        reader2.setId(2);
        reader2.setName("Adam");
        reader2.setSurname("Kowalczyk");
        reader2.setBirthDate(LocalDate.now().minusYears(5));

        readers.addAll(List.of(reader1, reader2));
    }

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        loanService = new LoanService(loansRepository, readerRepository, configurationService);
    }
}
