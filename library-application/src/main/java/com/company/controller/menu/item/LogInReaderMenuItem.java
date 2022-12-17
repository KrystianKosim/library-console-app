package com.company.controller.menu.item;

import com.company.controller.ReaderController;
import com.company.controller.menu.MainMenuController;
import com.company.controller.menu.utils.MenuAction;
import com.company.controller.menu.utils.MenuItem;
import com.company.repository.models.entity.BookEntity;
import com.company.utils.Terminal;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@MenuItem(code = "LOGIN READER", description = "Login reader Menu", parent = {"LOGIN"})
public class LogInReaderMenuItem extends MenuAction {

    private final Terminal terminal;
    private final ReaderController readerController;

    @Override
    public void action() {
        String readerId = terminal.getStringValue(Terminal.ENTER_ID);
        int id = 0;
        try {
            id = Integer.parseInt(readerId);
        } catch (NumberFormatException e) {
            action();
        }
        boolean isCorrectId = readerController.isCorrectReaderId(id);
        if (isCorrectId) {
            MainMenuController.setReaderId(id);
            MainMenuController.setUserType(Terminal.READER);
        } else {
            terminal.printMessage(Terminal.INCORRECT_LOGIN);
            action();
        }
        List<BookEntity> books = readerController.isReaderHaveBooksTooLong(id);
        if (!books.isEmpty()) {
            terminal.printMessage(Terminal.YOU_HAVE_TO_RETURN_BOOKS);
            terminal.displayList(books, Terminal.NOT_FOUND_BOOK_WITH_GIVEN_PARAMS);
        }
    }
}
