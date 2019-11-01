package com.company.codeSimplifier;

import com.company.handlers.MoveByEnterHandler;
import com.company.listeners.AlphabetListener;
import com.company.listeners.NumberListener;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public interface generalDesigners {
    static void nodeFormatter(TextField tfName, TextField tfFamily, TextField tfNationalID, Text txtWarning, Pane warningPane, GridPane informationPane, Node btSave) {
        tfName.setPromptText("First Name");
        tfFamily.setPromptText("Last Family");
        tfNationalID.setPromptText("National ID");

        tfName.setOnKeyPressed(new MoveByEnterHandler(tfFamily));
        tfFamily.setOnKeyPressed(new MoveByEnterHandler(tfNationalID));

        // Adding Listener to check Valid Input
        tfName.textProperty().addListener(new AlphabetListener(tfName, txtWarning));
        tfFamily.textProperty().addListener(new AlphabetListener(tfFamily, txtWarning));
        tfNationalID.textProperty().addListener(new NumberListener(tfNationalID, txtWarning));

        // Information Pane
        informationPane.addColumn(0, new Text("Name :"), new Text("Family :"), new Text("National ID :"));
        informationPane.addColumn(1, tfName, tfFamily, tfNationalID);
        informationPane.setPadding(new Insets(15));
        informationPane.setVgap(5);
        informationPane.setHgap(15);

// Warning Pane
        warningPane.setPrefSize(200, 0);
        warningPane.setPadding(new Insets(15));
        txtWarning.setWrappingWidth(150);
        warningPane.getChildren().add(txtWarning);
        txtWarning.setFont(Font.font("Arial Narrow", FontWeight.BOLD, FontPosture.ITALIC, 20));
        txtWarning.setFill(Color.RED);


    }
}
