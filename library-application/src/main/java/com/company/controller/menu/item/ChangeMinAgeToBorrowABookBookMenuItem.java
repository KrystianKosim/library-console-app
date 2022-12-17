package com.company.controller.menu.item;

import com.company.controller.ConfigurationController;
import com.company.controller.menu.utils.MenuAction;
import com.company.controller.menu.utils.MenuItem;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@MenuItem(code = "CHANGE MIN AGE", description = "Change min age to borrow a book", parent = {"CHANGE OPERATIONS"})
public class ChangeMinAgeToBorrowABookBookMenuItem extends MenuAction {

    private final ConfigurationController configurationController;

    @Override
    public void action() {
        configurationController.editMinAgeToBorrowABook();
    }
}
