package com.company.controller.menu.item;

import com.company.controller.menu.MainMenuController;
import com.company.controller.menu.utils.MenuAction;
import com.company.controller.menu.utils.MenuItem;


@MenuItem(code = "LOGOUT", description = "Logout", parent = {"ADMIN", "READER"}, priority = 2)
public class LogOutMenuItem extends MenuAction {

    public static final String MAIN = "MAIN";

    @Override
    public void action() {
        MainMenuController.setAction(MAIN);
        MainMenuController.setIsContinue(false);
        MainMenuController.deleteHistory();
        MainMenuController.deleteReaderId();
    }
}
