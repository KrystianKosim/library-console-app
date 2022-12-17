package com.company.controller.menu.item;

import com.company.controller.LoansController;
import com.company.controller.menu.MainMenuController;
import com.company.controller.menu.utils.MenuAction;
import com.company.controller.menu.utils.MenuItem;
import com.company.utils.Terminal;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@MenuItem(code = "DISPLAY LOANS", description = "Display loans", parent = {"READER", "READER OPERATIONS"})
public class DisplayLoansMenuItem extends MenuAction {

    private final LoansController loansController;
    private final Terminal terminal;

    @Override
    public void action() {
        Integer readerId = MainMenuController.getReaderId();
        if (readerId == null) {
            String readerIdString = terminal.getStringValue(Terminal.ENTER_ID);
            try {
                readerId = Integer.parseInt(readerIdString);
            } catch (NumberFormatException e) {
                terminal.printMessage(Terminal.INCORRECT_ID);
                return;
            }
        }
        loansController.displayLoans(readerId);
    }
}
