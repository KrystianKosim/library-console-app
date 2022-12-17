package com.company.controller;

import com.company.service.BookService;
import com.company.service.ConfigurationService;
import com.company.service.LoanService;
import com.company.utils.Terminal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class ConfigurationController {
    public static final String MAX_NUMBER_OF_DAYS_TO_BORROW_BOOK = "max number of days to borrow book";
    public static final String MAX_NUMBER_OF_BORROW_BOOKS_IN_THE_SAME_TIME = "max number of borrow books in the same time";
    public static final String MIN_AGE_TO_BORROW_A_BOOK = "min age to borrow a book";
    public static final String ADMIN = "ADMIN";
    private final Terminal terminal;

    private final LoanService loanService;
    private final BookService bookService;
    private final ConfigurationService configurationService;


    private static int maxNumberOfBorrowedBooks = 2;
    private static int maxNumberOfDaysToBorrowABook = 10;
    private static int minAgeToBorrowABook = 13;


    /**
     * Method to check if configuration rules are in configurationTable
     *
     * @return true if they are
     */
    public boolean isConfigurationDataInTable() {
        return configurationService.isConfigurationDataInTable();
    }

    /**
     * Method to edit value of max days which user can have book
     */
    public void editMaxDaysToBorrowBook() {
        maxNumberOfDaysToBorrowABook = terminal.getValueToConfigurationTable(MAX_NUMBER_OF_DAYS_TO_BORROW_BOOK);
        configurationService.editNumberOfDaysToBorrowABook(maxNumberOfDaysToBorrowABook);
    }

    /**
     * Method to edit value of max number of books which user can have in the same time
     */
    public void editMaxNumberOfBorrowedBooks() {
        maxNumberOfBorrowedBooks = terminal.getValueToConfigurationTable(MAX_NUMBER_OF_BORROW_BOOKS_IN_THE_SAME_TIME);
        configurationService.editNumberOfBorrowedBooks(maxNumberOfBorrowedBooks);
    }

    /**
     * Method to edit the minimum age a user must be to borrow a book
     */
    public void editMinAgeToBorrowABook() {
        minAgeToBorrowABook = terminal.getValueToConfigurationTable(MIN_AGE_TO_BORROW_A_BOOK);
        configurationService.editMinAgeToBorrowABook(minAgeToBorrowABook);
    }

    /**
     * Method to set a rules of borrow a book, if they are not in the database
     */
    public void setDefaultRulesOfBorrow() {
        configurationService.addConfigurationDataToTable(maxNumberOfBorrowedBooks, maxNumberOfDaysToBorrowABook, minAgeToBorrowABook);
    }

    /**
     * Method to initialize related variables with the terms of the loan with the values in the table
     */

    public void initRulesOfBorrow() {
        maxNumberOfBorrowedBooks = configurationService.getMaxNumberOfBorrowedBooks();
        maxNumberOfDaysToBorrowABook = configurationService.getMaxNumberOfDaysToBorrowABook();
        minAgeToBorrowABook = configurationService.getMinAgeToBorrowABook();
    }

    public void initApplication() {
        bookService.setNumberOfAvailableBooks();
        loanService.setNumberOfBorrowedBooksReader();
        loanService.setNumberOfEveryBorrowedBooksReader();
    }


    /**
     * Method check is current user is an admin, it's necessary while borrow a book
     *
     * @param history, history of action in menu
     * @return true if is an Admin
     */
    public boolean isAdmin(List<String> history) {
        return history.contains(ADMIN);
    }


}
