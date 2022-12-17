package com.company.controller.menu.item;

import com.company.controller.ReaderController;
import com.company.controller.menu.utils.MenuAction;
import com.company.controller.menu.utils.MenuItem;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@MenuItem(code = "SEARCH READER", description = "Search reader", parent = {"READER OPERATIONS"})
public class SearchReaderMenuItem extends MenuAction {

    private final ReaderController readerController;

    @Override
    public void action() {
        readerController.searchForAReaders();
    }
}
