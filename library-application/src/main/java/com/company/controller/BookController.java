package com.company.controller;

import com.company.repository.models.entity.BookEntity;
import com.company.service.BookService;
import com.company.service.LoanService;
import com.company.utils.Terminal;
import com.company.utils.ValuesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Controller
public class BookController {


    private final Terminal terminal;
    private final ValuesService valuesService;
    private final BookService bookService;
    private final LoanService loanService;


    /**
     * Method to edit book
     */
    public void editBook() {
        List<BookEntity> allBooks = bookService.findAllBooks();
        BookEntity book = searchForSingleBook(allBooks);
        if (book != null) {
            editBookWithNewParameters(book);
        }
    }

    /**
     * Method to search a single or multiple books
     */
    public void searchForABooksWithGivenParameters() {
        Map<String, String> bookData = terminal.getBookData(true, Terminal.ENTER_FILTER_VALUES);
        List<BookEntity> allBooks = bookService.findAllBooks();
        List<BookEntity> books = bookService.findBookByParameters(bookData.get(Terminal.ID), bookData.get(Terminal.TITLE),
                bookData.get(Terminal.AUTHOR_NAME), bookData.get(Terminal.AUTHOR_SURNAME), bookData.get(Terminal.QUANTITY), allBooks);

        terminal.displayList(books, Terminal.NOT_FOUND_BOOK_WITH_GIVEN_PARAMS);
    }


    /**
     * Method to delete a single book
     */
    public void deleteBook() {
        List<BookEntity> books = bookService.findAllBooks();
        BookEntity bookResult = searchForSingleBook(books);
        if (bookResult == null) {
            return;
        }
        int bookId = bookResult.getId();
        List loans = loanService.isBookInLoansTable(bookId);
        if (loans.size() > 0) {
            terminal.displayLoansForBook(loans);
            boolean deleteFromLoans = terminal.getDecisionValueForDeleteFromLoans();
            if (deleteFromLoans) {
                loanService.deleteBookFromLoans(bookId);
                bookService.deleteBook(bookId);
            }
        } else {
            bookService.deleteBook(bookId);
        }
    }

    /**
     * Method to add new book
     */
    public void addBook() {
        Map<String, String> bookData = terminal.getBookData(false, Terminal.ENTER_NEW_VALUES);
        if (!valuesService.checkQuantityValue(bookData.get(Terminal.QUANTITY))) {
            terminal.printMessage(Terminal.INCORRECT_VALUE);
        }
        bookService.addBook(bookData.get(Terminal.TITLE), bookData.get(Terminal.QUANTITY), bookData.get(Terminal.AUTHOR_NAME), bookData.get(Terminal.AUTHOR_SURNAME));
    }

    private BookEntity searchForSingleBook(List<BookEntity> books) {
        Map<String, String> bookData = terminal.getBookData(true, Terminal.ENTER_FILTER_VALUES);
        List<BookEntity> booksResult = bookService.findBookByParameters(bookData.get(Terminal.ID), bookData.get(Terminal.TITLE),
                bookData.get(Terminal.AUTHOR_NAME), bookData.get(Terminal.AUTHOR_SURNAME), bookData.get(Terminal.QUANTITY), books);
        if (booksResult.size() == 1) {
            return booksResult.get(0);
        } else if (booksResult.size() > 1) {
            terminal.printMessage(Terminal.FOUNDED_MORE_THAN_ONE_BOOK);
            return searchForSingleBook(booksResult);
        } else {
            terminal.printMessage(Terminal.NOT_FOUND_BOOK_WITH_GIVEN_PARAMS);
            return null;
        }
    }

    private void editBookWithNewParameters(BookEntity book) {
        Map<String, String> bookData = terminal.getBookData(false, Terminal.ENTER_NEW_VALUES);
        bookService.editBook(book, bookData.get(Terminal.TITLE), bookData.get(Terminal.AUTHOR_NAME), bookData.get(Terminal.AUTHOR_SURNAME), bookData.get(Terminal.AUTHOR_SURNAME));
    }

}
