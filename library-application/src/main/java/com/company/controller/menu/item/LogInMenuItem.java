package com.company.controller.menu.item;

import com.company.controller.menu.utils.MenuAction;
import com.company.controller.menu.utils.MenuItem;

@MenuItem(code = "LOGIN", description = "Login", parent = {"MAIN"})
public class LogInMenuItem extends MenuAction {

    @Override
    public void action() {
    }
}
