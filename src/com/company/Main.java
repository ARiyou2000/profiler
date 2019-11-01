package com.company;

import com.company.codeSimplifier.generalDesigners;
import com.company.db.beans.Person;
import com.company.handlers.*;
import javafx.application.Application;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {

    public static ObservableList<Person> data = FXCollections.observableArrayList();
    public static TableView<Person> tableView = new TableView<>();
    public static ObservableList<Person> selectedItem = tableView.getSelectionModel().getSelectedItems();

    public static TextField tfName = new TextField();
    public static TextField tfFamily = new TextField();
    public static TextField tfNationalID = new TextField();

    public static Text txtWarning = new Text(10, 35, "");

    @Override
    public void start(Stage primaryStage) {

        Button btSave = new Button("Save");
        Button btShowAll = new Button("Show All Information");
        Button btSearch = new Button("Search");

        Text txtWarning = new Text(10, 35, "");
        Pane warningPane = new Pane();
        GridPane informationPane = new GridPane();

// Button Pane
        SplitPane btSavePane = new SplitPane(btSave);
        SplitPane btSearchPane = new SplitPane(btSearch);
        SplitPane btShowAllPane = new SplitPane(btShowAll);
        VBox buttonsBox = new VBox(btSavePane, btSearchPane, btShowAllPane);
        buttonsBox.setPadding(new Insets(15));
        buttonsBox.setSpacing(10);

// Control Pane
        HBox controlPane = new HBox(informationPane, warningPane, buttonsBox);
        controlPane.setPadding(new Insets(15));

// Table view column
        TableColumn personIdCol = new TableColumn("Row");
        personIdCol.setMinWidth(25);
        personIdCol.setCellValueFactory(new PropertyValueFactory<>("personId"));
        TableColumn firstNameCol = new TableColumn("First Name");
        firstNameCol.setMinWidth(200);
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn lastNameCol = new TableColumn("Last Name");
        lastNameCol.setMinWidth(200);
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("family"));
        TableColumn nationalIdCol = new TableColumn("National ID");
        nationalIdCol.setMinWidth(200);
        nationalIdCol.setCellValueFactory(new PropertyValueFactory<>("nationalId"));

        tableView.getColumns().addAll(personIdCol, firstNameCol, lastNameCol, nationalIdCol);

// Table Pane
        Pane tablePane = new StackPane();
        tablePane.getChildren().add(tableView);

// Binding and Validation inputs
        BooleanBinding invalidTextInput_btSearch = tfName.textProperty().isEmpty().and(tfFamily.textProperty().isEmpty()
                .and(tfNationalID.textProperty().isEmpty()));
        btSearch.disableProperty().bind(invalidTextInput_btSearch);

        BooleanBinding invalidTextInput_btSave = tfName.textProperty().isEmpty().or(tfFamily.textProperty().isEmpty()
                .or(tfNationalID.textProperty().isEmpty().or(tfName.textProperty().length().lessThan(3)
                        .or(tfFamily.textProperty().length().lessThan(3).or(tfNationalID.textProperty().length().isNotEqualTo(10))))));
        btSave.disableProperty().bind(invalidTextInput_btSave);

// RightClick (Context)  menu
        ContextMenu contextMenu = new ContextMenu();
        MenuItem menuItemDelete = new MenuItem("Delete");
        MenuItem menuItemEdit = new MenuItem("Edit");
        contextMenu.getItems().addAll(menuItemEdit, menuItemDelete);

// Disable context menu Items in wrong place
        BooleanProperty noItemSelected = new SimpleBooleanProperty(true);
        selectedItem.addListener((ListChangeListener<Person>) c -> {
            try {
                c.getList().get(0);             //if no item selected cause exception. the result wont matter.
                noItemSelected.set(false);
            } catch (
                    IndexOutOfBoundsException e) {
                noItemSelected.set(true);
            }
        });
        menuItemDelete.disableProperty().bind(noItemSelected);
        menuItemEdit.disableProperty().bind(noItemSelected);

// Set Buttons and RightClick Action
        tableView.setOnContextMenuRequested(event -> contextMenu.show(tableView, event.getScreenX(), event.getScreenY()));
        tableView.setOnMouseClicked(event -> contextMenu.hide());

        menuItemDelete.setOnAction(new MenuItem_DeleteHandler());
        menuItemEdit.setOnAction(new MenuItem_EditHandler());

        btSave.setOnAction(new SaveButtonHandler());
        btSearch.setOnAction(new SearchButtonHandler());
        btShowAll.setOnAction(new ShowAllButtonHandler());

        tfNationalID.setOnKeyPressed(new MoveByEnterHandler(btSave, btSearch, btShowAll));

        btSave.setOnKeyPressed(new SelectByEnterHandler(btSave));
        btSearch.setOnKeyPressed(new SelectByEnterHandler(btSearch));
        btShowAll.setOnKeyPressed(new SelectByEnterHandler(btShowAll));

// Hints
        btSave.setTooltip(new Tooltip("Save fields"));
        btSavePane.setTooltip(new Tooltip("Fill all fields to enable"));
        btSave.disableProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                btSavePane.setTooltip(new Tooltip("Fill all fields to enable"));
            } else {
                btSavePane.setTooltip(new Tooltip(""));
            }
        });

        btSearch.setTooltip(new Tooltip("Search for related results"));
        btSearchPane.setTooltip(new Tooltip("Fill one field at least to enable"));
        btSave.disableProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                btSearchPane.setTooltip(new Tooltip("Fill one field at least to enable"));
            } else {
                btSearchPane.setTooltip(null);
            }
        });

        btShowAll.setTooltip(new Tooltip("Show all data"));

// Adding common properties
        generalDesigners.nodeFormatter(tfName, tfFamily, tfNationalID, txtWarning, warningPane, informationPane, btSave);
        setButtonsStyle(btSave, btShowAll, btSearch);


// main Pane
        VBox fullBox = new VBox(tablePane, controlPane);
        Scene scene = new Scene(fullBox);
//        scene.getStylesheets().add(this.getClass().getResource("stylesheetName.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Profiler");
        primaryStage.show();

// Primary focus
        tfName.requestFocus();
    }

    // set Common Style and setting for buttons
    private void setButtonsStyle(Button... buttons) {
        for (Button b : buttons) {
            b.setPrefSize(200, 50);
            b.setFont(Font.font("Ubuntu Mono", FontPosture.ITALIC, 16));
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}