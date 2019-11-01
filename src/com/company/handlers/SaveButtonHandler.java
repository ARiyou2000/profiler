package com.company.handlers;

import com.company.Main;
import com.company.db.beans.Person;
import com.company.table.PersonManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class SaveButtonHandler implements EventHandler<ActionEvent> {

    @Override
    public void handle(ActionEvent event) {
        Person bean = new Person();
        bean.setName(Main.tfName.getText());
        bean.setFamily(Main.tfFamily.getText());
        bean.setNationalId(Main.tfNationalID.getText());

        Alert sureToSave = new Alert(Alert.AlertType.CONFIRMATION);
        sureToSave.setHeaderText("Are you sure to save data?");
        sureToSave.setContentText("Press \"OK\" if want to save and \"Cancel\" if you don't.");
        Optional<ButtonType> result = sureToSave.showAndWait();
        if (result.get().getButtonData().isDefaultButton()) {
            PersonManager.insert(bean);
        }
    }
}
