package com.company.controller.menu.item;

import com.company.controller.menu.utils.MenuAction;
import com.company.controller.menu.utils.MenuItem;

@MenuItem(code = "AUTHOR OPERATIONS", description = "Operations on author", parent = {"ADMIN"})
public class AuthorOperationsMenuItem extends MenuAction {

    @Override
    public void action() {
    }
}
