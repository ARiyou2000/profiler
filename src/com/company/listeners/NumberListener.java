package com.company.listeners;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class NumberListener implements ChangeListener<String> {
    TextField tfCaller;
    Text warning;

    public NumberListener(TextField tfCaller, Text warning) {
        this.tfCaller = tfCaller;
        this.warning = warning;
    }

    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (!newValue.matches("\\d{0,10}?")) {
            tfCaller.setText(oldValue);
            if (tfCaller.getText().length() == 10) {
                warning.setText("National ID must be 10 character");
            } else {
                warning.setText("Please enter only numeric value");
            }
        } else {
            warning.setText("");
        }
    }
}
