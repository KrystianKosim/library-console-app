package com.company.controller.menu.item;

import com.company.controller.BookController;
import com.company.controller.menu.utils.MenuAction;
import com.company.controller.menu.utils.MenuItem;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@MenuItem(code = "SEARCH BOOK", description = "Search book", parent = {"READER", "ADMIN", "BOOK OPERATIONS", "MAIN"}, priority = 0)
public class SearchBookMenuItem extends MenuAction {

    private final BookController bookController;

    @Override
    public void action() {
        bookController.searchForABooksWithGivenParameters();
    }
}
