package com.company.listeners;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class AlphabetListener implements ChangeListener<String> {
    TextField tfCaller;
    Text warning;

    public AlphabetListener(TextField tfCaller, Text warning) {
        this.tfCaller = tfCaller;
        this.warning = warning;
    }

    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (!newValue.matches("[A-Za-z ]{0,20}?")) {
            tfCaller.setText(oldValue);
            if (tfCaller.getText().length() == 20) {
                warning.setText("Name and family can't be to long");
            } else {
                warning.setText("Please enter value of [A - Z] and Space(\" \") characters.");
            }
        } else {
            warning.setText("");
        }
    }
}
