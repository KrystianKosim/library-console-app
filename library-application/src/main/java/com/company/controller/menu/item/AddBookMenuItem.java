package com.company.controller.menu.item;

import com.company.controller.BookController;
import com.company.controller.menu.utils.MenuAction;
import com.company.controller.menu.utils.MenuItem;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@MenuItem(code = "ADD BOOK", description = "Add book", parent = {"BOOK OPERATIONS"})
public class AddBookMenuItem extends MenuAction {

    private final BookController bookController;

    @Override
    public void action() {
        bookController.addBook();
    }
}
