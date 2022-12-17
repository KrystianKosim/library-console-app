package com.company.service;

import com.company.repository.models.entity.AuthorEntity;
import com.company.repository.models.entity.BookEntity;
import com.company.repository.models.repository.AuthorRepository;
import com.company.repository.models.repository.BookRepository;
import com.company.repository.models.repository.LoansRepository;
import com.company.utils.ValuesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BookService {

    public static final String BY_ID = "byId";
    public static final String BY_TITLE = "byTitle";
    public static final String BY_QUANTITY = "byQuantity";
    public static final String BY_AUTHOR_NAME = "byAuthorName";
    public static final String BY_AUTHOR_SURNAME = "byAuthorSurname";
    public static final String EMPTY = "";
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final ValuesService valuesService;
    private final LoansRepository loansRepository;
    private List<BookEntity> allBooks = new LinkedList<>();

    /**
     * Method to add book to database
     *
     * @param title
     * @param quantityString
     * @param authorName
     * @param authorSurname
     */
    public boolean addBook(String title, String quantityString, String authorName, String authorSurname) {
        int quantity = Integer.parseInt(quantityString);
        Optional<AuthorEntity> author = authorRepository.findAuthorEntitiesByNameAndSurname(authorName, authorSurname);
        if (author.isPresent()) {
            BookEntity book = BookEntity.builder()
                    .title(title)
                    .authorId(author.get())
                    .quantity(quantity)
                    .build();
            bookRepository.save(book);
            setNumberOfAvailableBooks();
            return true;
        }
        return false;
    }

    /**
     * Method to set current number of available books
     */
    public void setNumberOfAvailableBooks() {
        List<BookEntity> books = bookRepository.findAll();
        setNumberOfAvailableBooks(books);
        allBooks = books;
    }

    /**
     * Method to find book by id
     *
     * @param id
     * @return
     */
    public Optional<BookEntity> findBookById(int id) {
        return bookRepository.findById(id);
    }

    /**
     * Method to delete book by id
     *
     * @param bookId
     */
    public void deleteBook(int bookId) {
        bookRepository.deleteById(bookId);
    }

    public List<BookEntity> findAllBooks() {
        if (allBooks.isEmpty()) {
            allBooks = bookRepository.findAll();
        }
        return allBooks;
    }

    /**
     * Method to edit book, you don't have to edit all parameters of book
     *
     * @param book
     * @param tittle
     * @param authorName
     * @param authorSurname
     * @param quantityString
     */
    public BookEntity editBook(BookEntity book, String tittle, String authorName, String authorSurname, String quantityString) {
        Map<String, Boolean> checkValues = checkValues(EMPTY, tittle, authorName, authorName, quantityString);
        book.setTitle(checkValues.get(BY_TITLE) ? tittle : book.getTitle());
        book.setQuantity(checkValues.get(BY_QUANTITY) ? Integer.parseInt(quantityString) : book.getQuantity());
        if (checkValues.get(BY_AUTHOR_NAME) && checkValues.get(BY_AUTHOR_SURNAME)) {
            Optional<AuthorEntity> author = authorRepository.findAuthorEntitiesByNameAndSurname(authorName, authorSurname);
            book.setAuthorId(author.orElse(book.getAuthorId()));
        }
        bookRepository.save(book);
        return book;
    }

    /**
     * Method to find book by parameters
     *
     * @param id
     * @param title
     * @param authorName
     * @param authorSurname
     * @param quantity
     * @param allBooks
     * @return
     */
    public List<BookEntity> findBookByParameters(String id, String title, String authorName, String authorSurname, String quantity, List<BookEntity> allBooks) {
        Map<String, Boolean> checkValues = checkValues(id, title, authorName, authorSurname, quantity);
        return allBooks.stream()
                .filter(book -> !checkValues.get(BY_ID) || book.getId() == Integer.parseInt(id))
                .filter(book -> !checkValues.get(BY_TITLE) || book.getTitle().contains(title))
                .filter(book -> {
                    if (checkValues.get(BY_AUTHOR_NAME) && checkValues.get(BY_AUTHOR_SURNAME)) {
                        Optional<AuthorEntity> author = authorRepository.findAuthorEntitiesByNameAndSurname(authorName, authorSurname);
                        if (author.isPresent()) {
                            return book.getAuthorId().equals(author.get());
                        }
                        return false;
                    }
                    return true;
                })
                .filter(book -> !checkValues.get(BY_QUANTITY) || book.getQuantity() == Integer.parseInt(quantity))
                .collect(Collectors.toList());
    }

    /**
     * Method to find which books reader have too much time
     *
     * @param listOfBooksIdWhichReaderHaveTooMuchTime id numbers of books which reader have to much time
     * @return list of Books which reader have too much time
     */
    public List<BookEntity> findBooksWhichReaderHaveTooMuchTime(List<Integer> listOfBooksIdWhichReaderHaveTooMuchTime) {
        List<BookEntity> books = bookRepository.findAll();
        for (Integer bookId : listOfBooksIdWhichReaderHaveTooMuchTime) {
            books = books.stream()
                    .filter(book -> book.getId().equals(bookId))
                    .collect(Collectors.toList());
        }
        return books;
    }

    private List<BookEntity> setNumberOfAvailableBooks(List<BookEntity> books) {
        books.stream()
                .forEach(book -> {
                    int numberOfCurrentlyBorrowBooks = getNumberOfCurrentlyBorrowBooks(book.getId());
                    int quantityAvailable = book.getQuantity() - numberOfCurrentlyBorrowBooks;
                    book.setQuantityAvailable(quantityAvailable);
                });
        return books;
    }

    private int getNumberOfCurrentlyBorrowBooks(int bookId) {
        return loansRepository.getNumberOfCurrentlyBorrowedBooks(bookId);
    }

    private Map<String, Boolean> checkValues(String id, String title, String authorName, String authorSurname, String quantity) {
        boolean byId = valuesService.checkIdValue(id);
        boolean byTitle = valuesService.checkStringValueToFind(title);
        boolean byQuantity = valuesService.checkQuantityValue(quantity);
        boolean byAuthorName = valuesService.checkStringValueToFind(authorName);
        boolean byAuthorSurname = valuesService.checkStringValueToFind(authorSurname);
        Map<String, Boolean> resultOfCheck = new HashMap<>();
        resultOfCheck.put(BY_ID, byId);
        resultOfCheck.put(BY_TITLE, byTitle);
        resultOfCheck.put(BY_QUANTITY, byQuantity);
        resultOfCheck.put(BY_AUTHOR_NAME, byAuthorName);
        resultOfCheck.put(BY_AUTHOR_SURNAME, byAuthorSurname);
        return resultOfCheck;
    }
}
