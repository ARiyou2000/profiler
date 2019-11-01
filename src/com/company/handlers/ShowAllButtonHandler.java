package com.company.handlers;

import com.company.table.PersonManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class ShowAllButtonHandler implements EventHandler<ActionEvent> {

    @Override
    public void handle(ActionEvent event) {
        PersonManager.displayAllRows();
    }
}
