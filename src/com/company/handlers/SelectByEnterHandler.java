package com.company.handlers;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class SelectByEnterHandler implements EventHandler<KeyEvent> {
    private Button button;

    public SelectByEnterHandler(Button button) {
        this.button = button;
    }

    @Override
    public void handle(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            button.fire();
        }
    }
}
