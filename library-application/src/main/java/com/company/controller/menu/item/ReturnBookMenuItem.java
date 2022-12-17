package com.company.controller.menu.item;

import com.company.controller.LoansController;
import com.company.controller.menu.MainMenuController;
import com.company.controller.menu.utils.MenuAction;
import com.company.controller.menu.utils.MenuItem;
import com.company.utils.Terminal;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@MenuItem(code = "RETURN BOOK", description = "RETURN BOOK", parent = {"READER OPERATIONS", "BOOK OPERATIONS"})
public class ReturnBookMenuItem extends MenuAction {

    private final LoansController loansController;
    private final Terminal terminal;


    @Override
    public void action() {
        String bookIdString = terminal.getStringValue(Terminal.ENTER_ID);
        try {
            Integer readerId = MainMenuController.getReaderId();
            if (readerId == null) {
                String readerIdString = terminal.getStringValue(Terminal.ENTER_ID);
                readerId = Integer.parseInt(readerIdString);
            }
            int bookId = Integer.parseInt(bookIdString);
            loansController.returnBook(readerId, bookId);
        } catch (NumberFormatException e) {
            terminal.printMessage(Terminal.INCORRECT_ID);
        }
    }
}
