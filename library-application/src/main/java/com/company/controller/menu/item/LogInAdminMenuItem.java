package com.company.controller.menu.item;

import com.company.controller.menu.MainMenuController;
import com.company.controller.menu.utils.MenuAction;
import com.company.controller.menu.utils.MenuItem;
import com.company.utils.Terminal;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@MenuItem(code = "LOGIN ADMIN", description = "Login admin Menu", parent = {"LOGIN"})
public class LogInAdminMenuItem extends MenuAction {

    public static final String ADMIN = "ADMIN";
    public static final String PASSWORD = "ADMIN";
    private final Terminal terminal;

    @Override
    public void action() {
        String password = terminal.getPassword();
        if (password.equals(PASSWORD)) {
            MainMenuController.setUserType(ADMIN);
        } else {
            terminal.printMessage(Terminal.INCORRECT_VALUE);
        }
    }
}
