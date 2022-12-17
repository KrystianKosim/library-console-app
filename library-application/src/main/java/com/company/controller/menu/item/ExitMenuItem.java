package com.company.controller.menu.item;

import com.company.controller.menu.MainMenuController;
import com.company.controller.menu.utils.MenuAction;
import com.company.controller.menu.utils.MenuItem;

@MenuItem(code = "EXIT", description = "Close application", parent = {"ADMIN", "READER", "LOGIN", "MAIN"}, priority = 3)
public class ExitMenuItem extends MenuAction {


    @Override
    public void action() {
        MainMenuController.setAp(false);
        System.out.println("Exit application");
    }
}
