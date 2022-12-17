package com.company.controller.menu.item;

import com.company.controller.menu.utils.MenuAction;
import com.company.controller.menu.utils.MenuItem;

@MenuItem(code = "BOOK OPERATIONS", description = "Operations on book", parent = {"ADMIN"})
public class BookOperationsMenuItem extends MenuAction {

    @Override
    public void action() {
    }
}
