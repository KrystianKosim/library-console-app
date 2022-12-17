package com.company.controller;


import com.company.repository.models.entity.BookEntity;
import com.company.repository.models.entity.ReaderEntity;
import com.company.service.BookService;
import com.company.service.ConfigurationService;
import com.company.service.LoanService;
import com.company.service.ReaderService;
import com.company.utils.Terminal;
import com.company.utils.ValuesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class ReaderController {
    private final Terminal terminal;
    private final BookService bookService;
    private final ReaderService readerService;
    private final ValuesService valuesService;
    private final LoanService loanService;
    private final ConfigurationService configurationService;


    /**
     * Method to search a reader
     */
    public void searchForAReaders() {
        Map<String, String> readerData = terminal.getReaderData(true, true, Terminal.ENTER_FILTER_VALUES);
        LocalDate birthDate = valuesService.getBirthDate(readerData.get(Terminal.YEAR), readerData.get(Terminal.MONTH), readerData.get(Terminal.DAY));
        List<ReaderEntity> allReaders = readerService.findAllReaders();
        List<ReaderEntity> readers = readerService.findReaderByParameters(readerData.get(Terminal.ID), readerData.get(Terminal.NAME), readerData.get(Terminal.SURNAME), birthDate, allReaders);
        String type = readerData.get(Terminal.TYPE);
        if (type.equals(Terminal.PARENT)) {
            Map<String, String> parentData = terminal.getParentData();
            readers = readerService.findParentByParameters(parentData.get(Terminal.ADDRESS), parentData.get(Terminal.PHONE_NUMBER), readers);
        } else if (type.equals(Terminal.CHILD)) {
            Map<String, String> childData = terminal.getChildData();
            readers = readerService.findChildByParameters(childData.get(Terminal.PARENT_ID), readers);
        }
        terminal.displayList(readers, Terminal.NOT_FOUND_READER_WITH_GIVEN_PARAMETERS);
    }

    /**
     * Method to delete reader
     */
    public void deleteReader() {
        List<ReaderEntity> readers = readerService.findAllReaders();
        ReaderEntity readerResult = searchForReader(readers);
        if (readerResult == null) {
            return;
        }
        int readerId = readerResult.getId();
        List loans = loanService.isReaderInLoansTable(readerId);
        if (loans.size() > 0) {
            terminal.displayLoansForReader(loans);
            boolean deleteFromLoans = terminal.getDecisionValueForDeleteFromLoans();
            if (deleteFromLoans) {
                loanService.deleteReaderFromLoans(readerId);
                readerService.deleteReader(readerId);
            }
        } else {
            readerService.deleteReader(readerId);
        }
    }


    /**
     * Method to add new reader
     */
    public void addReader() {
        String type = terminal.getReaderType();
        if (type.equals(Terminal.PARENT)) {
            addParentReader();
        } else if (type.equals(Terminal.CHILD)) {
            addChildReader();
        }
    }

    /**
     * Method to check is reader have any book too long
     *
     * @param readerId, you want to check
     * @return , books which reader have or empty list
     */
    public List<BookEntity> isReaderHaveBooksTooLong(int readerId) {
        Optional<ReaderEntity> readerOptional = readerService.findReaderById(readerId);
        if (readerOptional.isEmpty()) {
            return new ArrayList<>();
        }
        ReaderEntity reader = readerOptional.get();
        List<Integer> booksId = readerService.booksIdWhichReaderHaveTooMuchTime(reader, configurationService.getMaxNumberOfDaysToBorrowABook());
        if (booksId.isEmpty()) {
            return List.of();
        }
        List<BookEntity> books = bookService.findBooksWhichReaderHaveTooMuchTime(booksId);
        return books;
    }


    /**
     * The method which check is reader can borrow a book (method checks him age, and current loans)
     *
     * @param readerId, which you want to check
     * @return true if reader can borrow a book
     */
    public boolean isReaderCanBorrowBook(Integer readerId) {
        Optional<ReaderEntity> readerOptional = readerService.findReaderById(readerId);
        if (readerOptional.isEmpty()) {
            return false;
        }
        ReaderEntity reader = readerOptional.get();
        int readerAge = LocalDate.now().getYear() - reader.getBirthDate().getYear();
        List<BookEntity> isReaderHaveBooksToLong = isReaderHaveBooksTooLong(reader.getId());
        if (reader.getNumberOfCurrentlyBorrowedBooks() >= configurationService.getMaxNumberOfBorrowedBooks()) {
            return false;
        } else if (readerAge < configurationService.getMinAgeToBorrowABook()) {
            return false;
        } else if (!isReaderHaveBooksToLong.isEmpty()) {
            return false;
        }
        return true;
    }


    /**
     * Method to check if the reader id is correct
     *
     * @param readerId, which you want to check
     * @return true if readerId is correct
     */
    public boolean isCorrectReaderId(int readerId) {
        Optional<ReaderEntity> readerEntityOptional = readerService.findReaderById(readerId);
        return readerEntityOptional.isPresent();
    }

    /**
     * Method to edit reader
     */

    public void editReader() {
        List<ReaderEntity> readers = readerService.findAllReaders();
        ReaderEntity reader = searchForReader(readers);
        if (reader == null) {
            return;
        }
        Map<String, String> readerData = terminal.getReaderData(false, true, Terminal.ENTER_FILTER_VALUES);
        LocalDate birthDate = valuesService.getBirthDate(readerData.get(Terminal.YEAR), readerData.get(Terminal.MONTH), readerData.get(Terminal.DAY));
        if (readerData.get(Terminal.TYPE).equals(Terminal.PARENT)) {
            Map<String, String> parentData = terminal.getParentData();
            readerService.editReaderToParent(reader, readerData.get(Terminal.NAME), readerData.get(Terminal.SURNAME), parentData.get(Terminal.ADDRESS), parentData.get(Terminal.PHONE_NUMBER), birthDate);
        } else if (readerData.get(Terminal.TYPE).equals(Terminal.CHILD)) {
            Map<String, String> childData = terminal.getChildData();
            readerService.editReaderToChild(reader, readerData.get(Terminal.NAME), readerData.get(Terminal.SURNAME), childData.get(Terminal.PARENT_ID), birthDate);
        } else {
            readerService.editReader(reader, readerData.get(Terminal.NAME), readerData.get(Terminal.SURNAME), birthDate);
        }
    }

    private ReaderEntity searchForReader(List<ReaderEntity> readers) {
        terminal.displayList(readers, Terminal.NOT_FOUND_READER_WITH_GIVEN_PARAMETERS);
        Map<String, String> readerData = terminal.getReaderData(true, true, Terminal.ENTER_FILTER_VALUES);
        LocalDate birthDate = valuesService.getBirthDate(readerData.get(Terminal.YEAR), readerData.get(Terminal.MONTH), readerData.get(Terminal.DAY));
        String type = readerData.get(Terminal.TYPE);
        List<ReaderEntity> readerResult = readerService.findReaderByParameters(readerData.get(Terminal.ID), readerData.get(Terminal.NAME), readerData.get(Terminal.SURNAME), birthDate, readers);
        if (readerResult.size() == 1) {
            return readerResult.get(0);
        } else if (readerResult.size() > 1) {
            if (type.equals(Terminal.PARENT)) {
                return searchForReaderParentType(readerResult);
            } else if (type.equals(Terminal.CHILD)) {
                return searchForReaderChildType(readerResult);
            }
        }
        terminal.printMessage(Terminal.NOT_FOUND_READER_WITH_GIVEN_PARAMETERS);
        return null;
    }


    private ReaderEntity searchForReaderChildType(List<ReaderEntity> readerResult) {
        String parentId = terminal.getStringValue(terminal.ENTER_ID);
        List<ReaderEntity> childResult = readerService.findChildByParameters(parentId, readerResult);
        if (childResult.size() == 1) {
            return childResult.get(0);
        } else if (childResult.size() > 1) {
            return searchForReader(childResult);
        }
        terminal.printMessage(Terminal.NOT_FOUND_READER_WITH_GIVEN_PARAMETERS);
        return null;
    }

    private ReaderEntity searchForReaderParentType(List<ReaderEntity> readerResult) {
        Map<String, String> parentData = terminal.getParentData();
        List<ReaderEntity> parentResult = readerService.findParentByParameters(parentData.get(Terminal.ADDRESS), parentData.get(Terminal.PHONE_NUMBER), readerResult);
        if (parentResult.size() == 1) {
            return parentResult.get(0);
        } else if (parentResult.size() > 1) {
            return searchForReader(parentResult);
        }
        terminal.printMessage(Terminal.NOT_FOUND_READER_WITH_GIVEN_PARAMETERS);
        return null;
    }

    private void addChildReader() {
        Map<String, String> readerData = terminal.getReaderData(false, false, Terminal.ENTER_NEW_VALUES);
        LocalDate birthDate = valuesService.getBirthDate(readerData.get(Terminal.YEAR), readerData.get(Terminal.MONTH), readerData.get(Terminal.DAY));
        if (birthDate == null) {
            terminal.printMessage(Terminal.INCORRECT_VALUE);
            return;
        }
        boolean knowParentId = terminal.isKnowParentId();
        int parentId;
        if (knowParentId) {
            Map<String, String> childData = terminal.getChildData();
            parentId = valuesService.getIdFromString(childData.get(Terminal.PARENT_ID));
        } else {
            List<ReaderEntity> readers = readerService.findAllReaders();
            ReaderEntity parent = searchForReader(readers);
            if (parent == null) {
                terminal.printMessage(Terminal.NOT_FOUND_PARENT_WITH_GIVEN_PARAMETERS);
                return;
            }
            parentId = parent.getId();
        }
        readerService.addChildReader(readerData.get(Terminal.NAME), readerData.get(Terminal.SURNAME), parentId, birthDate);
    }

    private void addParentReader() {
        Map<String, String> readerData = terminal.getReaderData(false, false, Terminal.ENTER_NEW_VALUES);
        Map<String, String> parentData = terminal.getParentData();
        LocalDate birthDate = valuesService.getBirthDate(readerData.get(Terminal.YEAR), readerData.get(Terminal.MONTH), readerData.get(Terminal.DAY));
        if (birthDate == null) {
            terminal.printMessage(Terminal.INCORRECT_VALUE);
            return;
        }
        readerService.addParentReader(readerData.get(Terminal.NAME), readerData.get(Terminal.SURNAME), parentData.get(Terminal.ADDRESS), parentData.get(Terminal.PHONE_NUMBER), birthDate);
    }
}
