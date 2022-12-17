package com.company.controller.menu.item;

import com.company.controller.AuthorController;
import com.company.controller.menu.utils.MenuAction;
import com.company.controller.menu.utils.MenuItem;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@MenuItem(code = "EDIT AUTHOR", description = "Edit author", parent = {"AUTHOR OPERATIONS"})
public class EditAuthorMenuItem extends MenuAction {

    private final AuthorController authorController;

    @Override
    public void action() {
        authorController.editAuthor();
    }
}
