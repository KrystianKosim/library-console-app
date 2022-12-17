package com.company.controller.menu.item;

import com.company.controller.ConfigurationController;
import com.company.controller.menu.utils.MenuAction;
import com.company.controller.menu.utils.MenuItem;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@MenuItem(code = "MAIN", description = "Main menu")
public class MainMenuItem extends MenuAction {

    private final ConfigurationController configurationController;

    @Override
    public void action() {
        configurationController.initApplication();
    }
}
