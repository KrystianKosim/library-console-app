package com.company.controller;

import com.company.repository.models.entity.BookEntity;
import com.company.repository.models.entity.ReaderEntity;
import com.company.service.BookService;
import com.company.service.LoanService;
import com.company.service.ReaderService;
import com.company.utils.Terminal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class LoansController {


    private final Terminal terminal;
    private final LoanService loanService;
    private final ReaderService readerService;
    private final BookService bookService;

    /**
     * Method to display loans
     *
     * @param readerId , which loans you want to display
     */
    public void displayLoans(int readerId) {
        List loans = loanService.isReaderInLoansTable(readerId);
        if (loans.size() > 0) {
            terminal.displayLoansForReader(loans);
        } else {
            terminal.printMessage(Terminal.YOU_HAVEN_T_GOT_ANY_LOANS);
        }
    }

    /**
     * Method to borrow a book
     *
     * @param readerId, the id of reader who borrows the book
     */
    public void borrowBook(int readerId) {
        ReaderEntity reader = readerService.findReaderById(readerId).orElse(null);
        if (reader == null) {
            return;
        }
        if (!loanService.isReaderCanLoanBook(reader)) {
            terminal.printMessage(Terminal.YOU_CAN_T_BORROW_A_BOOK);
            return;
        }
        List<BookEntity> books = bookService.findAllBooks();
        BookEntity book = searchForSingleBook(books);
        if (book == null) {
            return;
        }
        if (book.getQuantityAvailable() == 0) {
            terminal.printMessage(Terminal.BOOK_IS_NOT_AVAILABLE);
            return;
        }
        boolean isReaderHasABook = loanService.isReaderHasABook(readerId, book.getId());
        if (isReaderHasABook) {
            terminal.printMessage(Terminal.YOU_HAVE_THIS_BOOK);
            return;
        }
        int bookId = book.getId();
        loanService.addNewLoanIntoTable(bookId, readerId);
        loanService.setNumberOfBorrowedBooksReader();
        loanService.setNumberOfEveryBorrowedBooksReader();
        bookService.setNumberOfAvailableBooks();
    }

    /**
     * Method to return a book
     *
     * @param readerId, the id of reader who borrowed the book
     * @param bookId,   the id of book which is on loan
     */
    public void returnBook(Integer readerId, int bookId) {
        Optional<BookEntity> bookOptional = bookService.findBookById(bookId);
        if (bookOptional.isEmpty()) {
            return;
        }
        loanService.returnBook(readerId, bookId);
        bookService.setNumberOfAvailableBooks();
        loanService.setNumberOfBorrowedBooksReader();
    }

    /**
     * Method to set number of currently borrow books
     */
    public void setNumberOfBorrowedBooksReader() {
        loanService.setNumberOfBorrowedBooksReader();
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
}
