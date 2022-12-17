package com.company.controller.menu.item;

import com.company.controller.ConfigurationController;
import com.company.controller.LoansController;
import com.company.controller.ReaderController;
import com.company.controller.menu.MainMenuController;
import com.company.controller.menu.utils.MenuAction;
import com.company.controller.menu.utils.MenuItem;
import com.company.utils.Terminal;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@MenuItem(code = "BORROW BOOK", description = "Borrow a book", parent = {"BOOK OPERATIONS", "Reader Menu", "READER OPERATIONS", "READER"})
public class BorrowBookMenuItem extends MenuAction {

    private final Terminal terminal;
    private final ConfigurationController configurationController;
    private final ReaderController readerController;
    private final LoansController loansController;

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
        List<String> history = MainMenuController.getHistory();
        boolean isAdmin = configurationController.isAdmin(history);
        if (!isAdmin) {
            boolean isReaderCanBorrowBook = readerController.isReaderCanBorrowBook(readerId);
            if (!isReaderCanBorrowBook) {
                terminal.printMessage(Terminal.YOU_CAN_T_BORROW_A_BOOK);
                return;
            }
        }
        loansController.borrowBook(readerId);
        loansController.setNumberOfBorrowedBooksReader();
    }
}