package com.company.controller.menu.item;

import com.company.controller.menu.MainMenuController;
import com.company.controller.menu.utils.MenuAction;
import com.company.controller.menu.utils.MenuItem;

@MenuItem(code = "BACK", description = "Back to previous menu", parent = {"BOOK OPERATIONS", "READER OPERATIONS", "CHANGE OPERATIONS", "AUTHOR OPERATIONS"}, priority = 2)
public class BackMenuItem extends MenuAction {

    @Override
    public void action() {
        MainMenuController.setPreviousAction();
        MainMenuController.setIsContinue(false);
    }
}
