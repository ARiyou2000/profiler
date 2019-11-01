package com.company.handlers;

import com.company.Main;
import com.company.codeSimplifier.generalDesigners;
import com.company.db.beans.Person;
import com.company.table.PersonManager;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.util.Optional;

public class MenuItem_EditHandler implements EventHandler<ActionEvent> {

    @Override
    public void handle(ActionEvent event) {
        try {

            Text txtWarning = new Text(10, 35, "");
            Pane warningPane = new Pane();
            GridPane informationPane = new GridPane();



            Person bean = Main.selectedItem.get(0);
            Dialog<Person> editDialog = new Dialog<>();
            editDialog.setHeaderText("Edit Panel");
            editDialog.setContentText("Please fill the fields with valid value.");

            ButtonType btSaveType = new ButtonType("Save", ButtonBar.ButtonData.RIGHT);
            ButtonType btCancelType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            editDialog.getDialogPane().getButtonTypes().addAll(btSaveType, btCancelType);

// Add some text fields stuff
            TextField tfName = new TextField();
            TextField tfFamily = new TextField();
            TextField tfNationalID = new TextField();

            tfName.setText(bean.getName());
            tfFamily.setText(bean.getFamily());
            tfNationalID.setText(bean.getNationalId());

            tfNationalID.setOnKeyPressed(new MoveByEnterHandler(editDialog.getDialogPane().lookupButton(btSaveType),editDialog.getDialogPane().lookupButton(btCancelType)));





// Control (main)  Pane
            HBox controlPane = new HBox(informationPane, warningPane);
            controlPane.setPadding(new Insets(15));

            editDialog.getDialogPane().setContent(controlPane);

// Binding and Validation inputs
            BooleanBinding invalidTextInput_btSave = tfName.textProperty().isEmpty().or(tfFamily.textProperty().isEmpty()       ////
                    .or(tfNationalID.textProperty().isEmpty().or(tfName.textProperty().length().lessThan(3)             ////
                            .or(tfFamily.textProperty().length().lessThan(3).or(tfNationalID.textProperty().length().isNotEqualTo(10))))));
            editDialog.getDialogPane().lookupButton(btSaveType).disableProperty().bind(invalidTextInput_btSave);

// Set Buttons Action
            editDialog.getDialogPane().lookupButton(btSaveType).setOnKeyPressed(new SelectByEnterHandler(
                    (Button) editDialog.getDialogPane().lookupButton(btSaveType)));
            editDialog.getDialogPane().lookupButton(btCancelType).setOnKeyPressed(new SelectByEnterHandler(
                    (Button) editDialog.getDialogPane().lookupButton(btCancelType)));

            generalDesigners.nodeFormatter(tfName, tfFamily, tfNationalID, txtWarning, warningPane, informationPane, editDialog.getDialogPane().lookupButton(btSaveType));

// Primary focus
            tfName.requestFocus();

// Result convert
            editDialog.setResultConverter(new Callback<ButtonType, Person>() {
                @Override
                public Person call(ButtonType param) {
                    if (param == btSaveType) {
                        return new Person(tfName.getText(), tfFamily.getText(), tfNationalID.getText());
                    }
                    return null;
                }
            });

// Show dialog and use result
            Optional<Person> result = editDialog.showAndWait();
            if (result.isPresent()) {
                Alert sureToEdit = new Alert(Alert.AlertType.CONFIRMATION);
                sureToEdit.setHeaderText("Are you sure to save change to data?");
                sureToEdit.setContentText("Press \"OK\" if want to save and \"Cancel\" if you don't.");
                Optional<ButtonType> answer = sureToEdit.showAndWait();
                if (answer.get().getButtonData().isDefaultButton()) {
                    PersonManager.update(result.get());
                }
            }

        } catch (NullPointerException e) {
            Alert emptyRow = new Alert(Alert.AlertType.ERROR);
            emptyRow.setHeaderText("Empty Row");
            emptyRow.setContentText("Please select a valid row to edit.");
            emptyRow.showAndWait();
        }
    }
}
