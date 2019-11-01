package com.company.handlers;

import com.company.Main;
import com.company.table.PersonManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class MenuItem_DeleteHandler implements EventHandler<ActionEvent> {

    @Override
    public void handle(ActionEvent event) {
        try {
            Main.selectedItem.get(0).getName(); //Check if user select a item or not. Throws NullPointerException if not.

            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setHeaderText("Are you sure to delete ?");
            confirmation.setContentText("There is no way to restore data!");
            Optional<ButtonType> result = confirmation.showAndWait();
            if (result.get() == ButtonType.OK) {
                PersonManager.delete();
            }
        } catch (NullPointerException e) {
            Alert emptyRow = new Alert(Alert.AlertType.ERROR);
            emptyRow.setHeaderText("Empty Row");
            emptyRow.setContentText("Please select a valid row to be delete.");
            emptyRow.showAndWait();
        }
    }
}
