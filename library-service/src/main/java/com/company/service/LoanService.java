package com.company.service;

import com.company.repository.models.entity.ReaderEntity;
import com.company.repository.models.repository.LoansRepository;
import com.company.repository.models.repository.ReaderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@RequiredArgsConstructor
@Service
public class LoanService {

    private final LoansRepository loansRepository;
    private final ReaderRepository readerRepository;
    private final ConfigurationService configurationService;


    /**
     * Method to add new loan into database
     *
     * @param bookId
     * @param readerId
     */
    public void addNewLoanIntoTable(int bookId, int readerId) {
        loansRepository.addNewLoanIntoTable(bookId, readerId);
    }

    /**
     * Method to set a number of every borrowed books for every reader
     */
    public List<ReaderEntity> setNumberOfEveryBorrowedBooksReader() {
        List<ReaderEntity> readerEntities = readerRepository.findAll();
        return setNumberOfEveryBorrowedBooksReader(readerEntities);
    }


    /**
     * Method to initialization number of current borrow books as reader field
     */
    public List<ReaderEntity> setNumberOfBorrowedBooksReader() {
        List<ReaderEntity> readerEntities = readerRepository.findAll();
        return setNumberOfBorrowedBooks(readerEntities);
    }


    /**
     * Method to delete book from loans history
     *
     * @param bookId
     */
    public void deleteBookFromLoans(int bookId) {
        loansRepository.deleteBookFromLoans(bookId);
    }

    /**
     * Method to check if book is already in loan table, if it is return list of loans
     *
     * @param bookId
     * @return
     */
    public List isBookInLoansTable(int bookId) {
        return loansRepository.isBookInLoansTable(bookId);
    }

    /**
     * Method to check if reader is already in loan table, if it is return list of loans
     *
     * @param readerId
     * @return
     */
    public List isReaderInLoansTable(int readerId) {
        return loansRepository.isReaderInLoansTable(readerId);
    }

    /**
     * Method to delte reader from loan history
     *
     * @param readerId
     */
    public void deleteReaderFromLoans(int readerId) {
        loansRepository.deleteReaderFromLoans(readerId);
    }

    /**
     * Method to return book into library and set return date
     *
     * @param readerId
     * @param bookId
     */
    public void returnBook(Integer readerId, int bookId) {
        loansRepository.returnBook(readerId, bookId);
    }


    /**
     * Method to check if reader already loan a book
     *
     * @param readerId
     * @param id
     * @return
     */
    public boolean isReaderHasABook(int readerId, Integer id) {
        return loansRepository.isReaderHasABook(readerId, id);
    }

    /**
     * Method to check if reader can loan a book
     *
     * @param reader
     * @return
     */
    public boolean isReaderCanLoanBook(ReaderEntity reader) {
        int readerId = reader.getId();
        int borrowedBooks = getNumberOfBorrowedBooks(readerId);

        int yearOfBirth = reader.getBirthDate().getYear();
        int age = LocalDate.now().getYear() - yearOfBirth;

        int maxNumberOfBorrowedBooks = configurationService.getMaxNumberOfBorrowedBooks();
        int minAgeToBorrowABook = configurationService.getMinAgeToBorrowABook();

        return borrowedBooks < maxNumberOfBorrowedBooks && age >= minAgeToBorrowABook;
    }

    /**
     * Method to set number of currently borrow books for every reader
     *
     * @param allReaders
     * @return
     */
    private List<ReaderEntity> setNumberOfBorrowedBooks(List<ReaderEntity> allReaders) {
        allReaders.stream()
                .forEach(reader -> {
                    int numberOfCurrentlyBorrowedBooks = getNumberOfBorrowedBooks(reader.getId());
                    reader.setNumberOfCurrentlyBorrowedBooks(numberOfCurrentlyBorrowedBooks);
                });
        return allReaders;
    }

    /**
     * Method to get number of currently borrow books of single reader
     *
     * @param readerId
     * @return
     */
    private int getNumberOfBorrowedBooks(int readerId) {
        return loansRepository.getNumberOfCurrentlyBorrowedBooksForReader(readerId);
    }

    private List<ReaderEntity> setNumberOfEveryBorrowedBooksReader(List<ReaderEntity> allReaders) {
        allReaders.stream()
                .forEach(reader -> {
                    int numberOfEveryBorrowedBooks = getNumberOfEveryBorrowedBooks(reader.getId());
                    reader.setNumberOfEveryBorrowedBooks(numberOfEveryBorrowedBooks);
                });
        return allReaders;
    }

    private int getNumberOfEveryBorrowedBooks(Integer id) {
        return loansRepository.getNumberOfEveryBorrowedBooksForReader(id);
    }


}
