package com.company.controller.menu.item;

import com.company.controller.ReaderController;
import com.company.controller.menu.utils.MenuAction;
import com.company.controller.menu.utils.MenuItem;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@MenuItem(code = "DELETE READER", description = "Delete reader", parent = {"READER OPERATIONS"})
public class DeleteReaderMenuItem extends MenuAction {

    private final ReaderController readerController;

    @Override
    public void action() {
        readerController.deleteReader();
    }
}
