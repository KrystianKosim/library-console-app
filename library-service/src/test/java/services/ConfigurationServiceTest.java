package services;

import com.rbinternational.repository.models.repository.ConfigurationRepository;
import com.rbinternational.service.ConfigurationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestExecutionListeners(MockitoTestExecutionListener.class)
@ContextConfiguration
@AutoConfigureMockMvc
public class ConfigurationServiceTest {

    @Mock
    private ConfigurationRepository configurationRepository;
    @InjectMocks
    private ConfigurationService configurationService;

    @Test
    @DisplayName("Should return true because configuration is in data table")
    void shouldReturnTrueBecauseConfigurationIsInDataTable() {
        when(configurationRepository.isConfigurationDataInTable()).thenReturn(true);

        boolean result = configurationService.isConfigurationDataInTable();

        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("Should edit number of days to borrow a book")
    void shouldEditNumberOfDaysToBorrowABook() {
        int maxNumberOfDaysToBorrowABook = 2;

        boolean result = configurationService.editNumberOfDaysToBorrowABook(maxNumberOfDaysToBorrowABook);

        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("Should edit max number of borrowed books")
    void shouldEditMaxNumberOfBorrowedBooks() {
        int maxNumberOfBorrowedBooks = 3;

        boolean result = configurationService.editNumberOfBorrowedBooks(maxNumberOfBorrowedBooks);

        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("Should edit min age to borrow a book")
    void shouldEditMinAgeToBorrowABook() {
        int minAgeToBorrowABook = 10;

        boolean result = configurationService.editMinAgeToBorrowABook(minAgeToBorrowABook);

        Assertions.assertTrue(result);
    }


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        configurationService = new ConfigurationService(configurationRepository);
    }
}
