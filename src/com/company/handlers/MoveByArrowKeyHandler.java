package com.company.handlers;


import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class MoveByArrowKeyHandler {

    private Node[] top;
    private Node[] right;
    private Node[] bottom;
    private Node[] left;

    public void setTop(Node... top) {
        this.top = top;
    }

    public void setRight(Node... right) {
        this.right = right;
    }

    public void setBottom(Node... bottom) {
        this.bottom = bottom;
    }

    public void setLeft(Node... left) {
        this.left = left;
    }

    public void moveByArrows(KeyCode keyCode) {
        switch (keyCode) {
            case UP:
                firstAvailable(top);
                break;
            case RIGHT:
                firstAvailable(right);
                break;
            case DOWN:
                firstAvailable(bottom);
                break;
            case LEFT:
                firstAvailable(left);
                break;
        }
    }

    static void firstAvailable(Node... nodes) {
        for (Node node : nodes) {
            if (!node.isDisable()) {
                node.requestFocus();
                break;
            }
        }
    }
}


/*
    private MoveByArrowKeyHandler moveByArrowKeyHandler = new MoveByArrowKeyHandler();   //Inherit is better or composition???

    public void setTop(Node... top) {
        moveByArrowKeyHandler.setTop(top);
    }

    public void setRight(Node... right) {
        moveByArrowKeyHandler.setRight(right);
    }

    public void setBottom(Node... bottom) {
        moveByArrowKeyHandler.setBottom(bottom);
    }

    public void setLeft(Node... left) {
        moveByArrowKeyHandler.setLeft(left);
    }
 */