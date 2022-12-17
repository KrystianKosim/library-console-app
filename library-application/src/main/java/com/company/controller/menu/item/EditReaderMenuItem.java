package com.company.controller.menu.item;

import com.company.controller.ReaderController;
import com.company.controller.menu.utils.MenuAction;
import com.company.controller.menu.utils.MenuItem;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@MenuItem(code = "EDIT READER", description = "Edit reader", parent = {"READER OPERATIONS"})
public class EditReaderMenuItem extends MenuAction {

    private final ReaderController readerController;

    @Override
    public void action() {
        readerController.editReader();
    }
}
