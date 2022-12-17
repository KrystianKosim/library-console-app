package com.company.controller;

import com.company.repository.models.entity.AuthorEntity;
import com.company.repository.models.entity.BookEntity;
import com.company.service.AuthorService;
import com.company.utils.Terminal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Controller
public class AuthorController {


    private final Terminal terminal;
    private final AuthorService authorService;

    /**
     * Method to search for a single or multiple authors in database
     */
    public void searchForAnAuthors() {
        Map<String, String> authorData = terminal.getAuthorData(true, Terminal.ENTER_FILTER_VALUES);
        List<AuthorEntity> authors = authorService.findAllAuthors();
        List<AuthorEntity> authorResult = authorService.findAuthors(authorData.get(Terminal.ID), authorData.get(Terminal.NAME), authorData.get(Terminal.SURNAME), authors);
        terminal.displayList(authorResult, Terminal.NOT_FOUND_AUTHOR_WITH_GIVEN_PARAMS);
    }


    /**
     * Method to edit author
     */
    public void editAuthor() {
        List<AuthorEntity> authors = authorService.findAllAuthors();
        editAuthor(authors);
    }

    /**
     * Method to delete author
     */
    public void deleteAuthor() {
        List<AuthorEntity> authors = authorService.findAllAuthors();
        AuthorEntity authorResult = searchForAuthor(authors);
        if (authorResult == null) {
            return;
        }
        List<BookEntity> isAuthorHaveBooks = authorService.isAuthorHaveBooks(authorResult);
        if (isAuthorHaveBooks.isEmpty()) {
            authorService.deleteAuthor(authorResult);
        } else {
            terminal.printMessage(Terminal.AUTHOR_HAVE_BOOKS_YOU_CAN_T_DELETE_HIM);
        }
    }

    /**
     * Method to add new author
     */
    public void addAuthor() {
        Map<String, String> authorData = terminal.getAuthorData(false, Terminal.ENTER_NEW_VALUES);
        authorService.addAuthor(authorData.get(Terminal.NAME), authorData.get(Terminal.SURNAME));
    }

    /**
     * Method to get a single author from repository
     *
     * @param authors
     * @return
     */
    private AuthorEntity searchForAuthor(List<AuthorEntity> authors) {
        terminal.displayList(authors, Terminal.NOT_FOUND_AUTHOR_WITH_GIVEN_PARAMS);
        Map<String, String> authorData = terminal.getAuthorData(true, Terminal.ENTER_FILTER_VALUES);
        List<AuthorEntity> authorResult = authorService.findAuthors(authorData.get(Terminal.ID), authorData.get(Terminal.NAME), authorData.get(Terminal.SURNAME), authors);
        if (authorResult.size() == 1) {
            return authorResult.get(0);
        } else if (authorResult.size() > 1) {
            return searchForAuthor(authorResult);
        }
        terminal.printMessage(Terminal.NOT_FOUND_AUTHOR_WITH_GIVEN_PARAMS);
        return null;
    }

    private void editAuthor(List<AuthorEntity> authors) {
        AuthorEntity authorToEdit = searchForAuthor(authors);
        if (authorToEdit == null) {
            terminal.printMessage(Terminal.NOT_FOUND_AUTHOR_WITH_GIVEN_PARAMS);
        }
        Map<String, String> authorData = terminal.getAuthorData(false, Terminal.ENTER_NEW_VALUES);
        authorService.editAuthor(authorData.get(Terminal.NAME), authorData.get(Terminal.SURNAME), authorToEdit);
    }
}
