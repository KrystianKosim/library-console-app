package com.company.service;

import com.company.repository.models.entity.AuthorEntity;
import com.company.repository.models.entity.BookEntity;
import com.company.repository.models.repository.AuthorRepository;
import com.company.repository.models.repository.BookRepository;
import com.company.utils.ValuesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AuthorService {

    public static final String BY_ID = "byId";
    public static final String BY_NAME = "byName";
    public static final String BY_SURNAME = "bySurname";
    public static final String EMPTY = "";
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final ValuesService valuesService;

    /**
     * Method to add author to database
     *
     * @param name
     * @param surname
     */
    public boolean addAuthor(String name, String surname) {
        boolean ifAuthorValuesAreCorrect = ifAuthorValuesAreCorrectToAdd(name, surname);
        if (ifAuthorValuesAreCorrect) {
            AuthorEntity author = AuthorEntity.builder()
                    .name(name)
                    .surname(surname)
                    .build();
            authorRepository.save(author);
            return true;
        }
        return false;
    }


    /**
     * Method to get all authors from repository
     *
     * @return
     */
    public List<AuthorEntity> findAllAuthors() {
        return authorRepository.findAll();
    }

    /**
     * Method to find multiple authors with given parameters
     *
     * @param id
     * @param name
     * @param surname
     * @param authors
     * @return
     */
    public List<AuthorEntity> findAuthors(String id, String name, String surname, List<AuthorEntity> authors) {
        Map<String, Boolean> checkValues = checkValues(id, name, surname);
        return authors.stream()
                .filter(author -> !checkValues.get(BY_ID) || author.getId() == Integer.parseInt(id))
                .filter(author -> !checkValues.get(BY_NAME) || author.getName().contains(name))
                .filter(author -> !checkValues.get(BY_SURNAME) || author.getSurname().contains(surname))
                .collect(Collectors.toList());
    }

    /**
     * Method to edit author, you can change not all parameters
     *
     * @param name
     * @param surname
     * @param author
     */
    public AuthorEntity editAuthor(String name, String surname, AuthorEntity author) {
        Map<String, Boolean> checkValues = checkValues(EMPTY, name, surname);
        if (checkValues.get(BY_NAME) || checkValues.get(BY_SURNAME)) {
            author.setName(checkValues.get(BY_NAME) ? name : author.getName());
            author.setSurname(checkValues.get(BY_SURNAME) ? surname : author.getSurname());
            authorRepository.save(author);
            return author;
        }
        return author;
    }

    /**
     * Method to delete single author
     *
     * @param author
     */
    public boolean deleteAuthor(AuthorEntity author) {
        int authorId = author.getId();
        authorRepository.deleteById(authorId);
        return true;
    }

    /**
     * Method to check is  author wrote books
     *
     * @param author, whose you want to check
     * @return list of book which author wrote or null if author didn't wrote any book
     */
    public List<BookEntity> isAuthorHaveBooks(AuthorEntity author) {
        Optional<List<BookEntity>> books = bookRepository.findBookEntitiesByAuthorId(author);
        return books.orElse(null);
    }

    private Map<String, Boolean> checkValues(String id, String name, String surname) {
        boolean byId = valuesService.checkIdValue(id);
        boolean byName = valuesService.checkStringValueToFind(name);
        boolean bySurname = valuesService.checkStringValueToFind(surname);
        Map<String, Boolean> resultOfCheck = new HashMap<>();
        resultOfCheck.put(BY_ID, byId);
        resultOfCheck.put(BY_NAME, byName);
        resultOfCheck.put(BY_SURNAME, bySurname);
        return resultOfCheck;
    }

    private boolean ifAuthorValuesAreCorrectToAdd(String name, String surname) {
        boolean ifNameCorrect = valuesService.checkStringValueToFind(name);
        boolean ifSurnameCorrect = valuesService.checkStringValueToFind(surname);
        return ifNameCorrect && ifSurnameCorrect;
    }

}
