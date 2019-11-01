package com.company.handlers;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class MoveByEnterHandler implements EventHandler<KeyEvent> {

    private Node[] nodes;

    public MoveByEnterHandler(Node... nodes) {
        this.nodes = nodes;
    }

    @Override
    public void handle(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            for (Node node : nodes) {
                if (!node.isDisable()) {
                    node.requestFocus();
                    break;
                }
            }
        }
    }
}
