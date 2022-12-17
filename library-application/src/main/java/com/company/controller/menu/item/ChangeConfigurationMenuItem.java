package com.company.controller.menu.item;

import com.company.controller.menu.utils.MenuAction;
import com.company.controller.menu.utils.MenuItem;

@MenuItem(code = "CHANGE OPERATIONS", description = "Change configuration", parent = {"ADMIN"})
public class ChangeConfigurationMenuItem extends MenuAction {

    @Override
    public void action() {
    }
}
