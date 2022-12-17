package com.company.repository.models.repository;

import com.company.repository.models.entity.ReaderEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class LoansRepository {

    public static final String READER_ID = "readerId";
    public static final String BOOK_ID = "bookId";
    public static final String DATE = "date";
    private final EntityManager entityManager;


    public boolean isReaderHasABook(int readerId, int bookId) {
        String sqlStatement = "SELECT * FROM loans WHERE reader_id=:readerId AND book_id=:bookId AND returneddate IS NULL";
        Query query = entityManager.createNativeQuery(sqlStatement)
                .setParameter(READER_ID, readerId)
                .setParameter(BOOK_ID, bookId);
        List results = query.getResultList();
        return results.size() != 0;
    }

    @Transactional
    public void returnBook(Integer readerId, int bookId) {
        LocalDate returnDate = LocalDate.now();
        String sqlStatement = "UPDATE loans SET returneddate=:date WHERE book_id=:bookId AND reader_id=:readerId AND returneddate IS NULL";
        entityManager.createNativeQuery(sqlStatement)
                .setParameter(DATE, returnDate)
                .setParameter(BOOK_ID, bookId)
                .setParameter(READER_ID, readerId)
                .executeUpdate();
    }

    @Transactional
    public void addNewLoanIntoTable(int bookId, int readerId) {
        String sqlStatement = "INSERT INTO loans VALUES (:bookId,:readerId)";
        entityManager.createNativeQuery(sqlStatement)
                .setParameter(BOOK_ID, bookId)
                .setParameter(READER_ID, readerId)
                .executeUpdate();
    }

    public List<Object[]> booksIdWhichReaderHaveTooMuchTime(ReaderEntity reader, int maxNumberOfDaysToBorrowABook) {
        int readerId = reader.getId();
        String sqlStatement = "SELECT book_id,borrowdate FROM loans WHERE reader_id=:readerId AND returneddate IS NULL";
        Query query = entityManager.createNativeQuery(sqlStatement).setParameter(READER_ID, readerId);
        List<Object[]> allResults = query.getResultList();
        return allResults;
    }

    public int getNumberOfCurrentlyBorrowedBooks(int bookId) {
        String sqlStatement = "SELECT COUNT(*) FROM loans WHERE book_id=:bookId AND returneddate IS NULL";
        Query query = entityManager.createNativeQuery(sqlStatement)
                .setParameter(BOOK_ID, bookId);
        BigInteger bigInteger = (BigInteger) query.getSingleResult();
        int numberOfCurrentlyBorrowedBooks = bigInteger.intValue();
        return numberOfCurrentlyBorrowedBooks;
    }

    public int getNumberOfCurrentlyBorrowedBooksForReader(int readerId) {
        String sqlStatement = "SELECT COUNT(*) FROM loans WHERE reader_id=:readerId AND returneddate IS NULL";
        Query query = entityManager.createNativeQuery(sqlStatement).setParameter(READER_ID, readerId);
        BigInteger bigInteger = (BigInteger) query.getSingleResult();
        int numberOfBorrowed = bigInteger.intValue();
        return numberOfBorrowed;
    }

    public int getNumberOfEveryBorrowedBooksForReader(int readerId) {
        String sqlStatement = "SELECT COUNT(*) FROM loans WHERE reader_id=:readerId";
        Query query = entityManager.createNativeQuery(sqlStatement)
                .setParameter(READER_ID, readerId);
        BigInteger bigInteger = (BigInteger) query.getSingleResult();
        int numberOfEveryBorrowedBooks = bigInteger.intValue();
        return numberOfEveryBorrowedBooks;
    }

    public List isBookInLoansTable(int bookId) {
        String sqlStatement = "SELECT l.book_id,l.reader_id,l.borrowdate,l.returneddate,r.name,r.surname FROM loans l RIGHT JOIN reader r ON l.reader_id=r.id WHERE book_id=:bookId";
        Query query = entityManager.createNativeQuery(sqlStatement).setParameter(BOOK_ID, bookId);
        List results = query.getResultList();
        return results;
    }

    public List isReaderInLoansTable(int readerId) {
        String sqlStatement = "SELECT l.book_id,l.reader_id,l.borrowdate,l.returneddate,b.title,b.author_id FROM loans l LEFT JOIN book b ON l.book_id=b.id WHERE reader_id=:readerId";
        Query query = entityManager.createNativeQuery(sqlStatement).setParameter(READER_ID, readerId);
        List results = query.getResultList();
        return results;
    }

    @Transactional
    public void deleteReaderFromLoans(int readerId) {
        String sqlStatement = "DELETE FROM loans WHERE reader_id=:readerId";
        Query query = entityManager.createNativeQuery(sqlStatement).setParameter(READER_ID, readerId);
        query.executeUpdate();
    }

    @Transactional
    public void deleteBookFromLoans(int bookId) {
        String sqlStatement = "DELETE FROM loans WHERE book_id=:bookId";
        Query query = entityManager.createNativeQuery(sqlStatement).setParameter(BOOK_ID, bookId);
        query.executeUpdate();
    }


}
