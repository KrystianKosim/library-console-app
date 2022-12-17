package com.company.controller.menu.item;

import com.company.controller.BookController;
import com.company.controller.menu.utils.MenuAction;
import com.company.controller.menu.utils.MenuItem;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@MenuItem(code = "DELETE BOOK", description = "Delete book", parent = {"BOOK OPERATIONS"})
public class DeleteBookMenuItem extends MenuAction {

    private final BookController bookController;

    @Override
    public void action() {
        bookController.deleteBook();
    }
}
