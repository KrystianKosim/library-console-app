package com.company.service;

import com.company.repository.models.repository.ConfigurationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ConfigurationService {

    private final ConfigurationRepository configurationRepository;

    /**
     * Method to check is configuration
     *
     * @return
     */
    public boolean isConfigurationDataInTable() {
        return configurationRepository.isConfigurationDataInTable();
    }

    /**
     * Method to edit a value of maximum holding length of the book
     *
     * @param maxNumberOfDaysToBorrowABook
     */

    public boolean editNumberOfDaysToBorrowABook(int maxNumberOfDaysToBorrowABook) {
        configurationRepository.editNumberOfDaysToBorrowABook(maxNumberOfDaysToBorrowABook);
        return true;
    }

    /**
     * Method to edit a value of maximum number of borrowed books in the same time
     *
     * @param maxNumberOfBorrowedBooks
     */
    public boolean editNumberOfBorrowedBooks(int maxNumberOfBorrowedBooks) {
        configurationRepository.editNumberOfBorrowedBooks(maxNumberOfBorrowedBooks);
        return true;
    }

    /**
     * Method to edit a value of minimum age to borrow a book
     *
     * @param minAgeToBorrowABook
     */
    public boolean editMinAgeToBorrowABook(int minAgeToBorrowABook) {
        configurationRepository.editMinAgeToBorrowABook(minAgeToBorrowABook);
        return true;
    }

    /**
     * Method to add loan rules into configuration datatable
     *
     * @param maxNumberOfBorrowedBooks
     * @param maxNumberOfDaysToBorrowABook
     * @param minAgeToBorrowABook
     */
    public void addConfigurationDataToTable(int maxNumberOfBorrowedBooks, int maxNumberOfDaysToBorrowABook, int minAgeToBorrowABook) {
        configurationRepository.addConfigurationDataToTable(maxNumberOfBorrowedBooks, maxNumberOfDaysToBorrowABook, minAgeToBorrowABook);
    }

    /**
     * Method to get max number of borrowed books in the same time from configuration table
     *
     * @return
     */
    public int getMaxNumberOfBorrowedBooks() {
        return configurationRepository.getMaxNumberOfBorrowedBooks();
    }

    /**
     * Method to get of maximum holding length of the book from configuration table
     *
     * @return
     */

    public int getMaxNumberOfDaysToBorrowABook() {
        return configurationRepository.getMaxNumberOfDaysToBorrowABook();
    }

    /**
     * Method to get minimum age to borrow a book from configuration table
     *
     * @return
     */
    public int getMinAgeToBorrowABook() {
        return configurationRepository.getMinAgeToBorrowABook();
    }

}
