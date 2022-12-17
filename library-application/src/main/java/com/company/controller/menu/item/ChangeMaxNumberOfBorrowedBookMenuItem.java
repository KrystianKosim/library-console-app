package com.company.controller.menu.item;

import com.company.controller.ConfigurationController;
import com.company.controller.menu.utils.MenuAction;
import com.company.controller.menu.utils.MenuItem;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@MenuItem(code = "CHANGE MAX BOOKS", description = "Change max number of borrowed book", parent = {"CHANGE OPERATIONS"})
public class ChangeMaxNumberOfBorrowedBookMenuItem extends MenuAction {

    private final ConfigurationController configurationController;

    @Override
    public void action() {
        configurationController.editMaxNumberOfBorrowedBooks();
    }
}
