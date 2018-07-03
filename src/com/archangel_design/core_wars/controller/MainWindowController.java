package com.archangel_design.core_wars.controller;

import com.archangel_design.core_wars.model.MainWindowModel;
import com.archangel_design.core_wars.utils.Alerts;
import com.archangel_design.core_wars.utils.CellType;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;

import javax.swing.text.html.ImageView;

public class MainWindowController {

    @FXML
    Label statusText;

    @FXML
    ImageView imgBomb;

    @FXML
    Canvas mapCanvas;

    @FXML
    ListView<String> bugList;

    MainWindowModel model;

    Stage parentStage;

    private void updateStatusText(String text) {
        statusText.setText(text);
    }

    public void onBombClicked() {
        model.setCurrentTool(CellType.MINE);
        updateStatusText("Current tool: Mine");
    }

    public void onEmptyClicked() {
        model.setCurrentTool(CellType.EMPTY);
        updateStatusText("Current tool: Empty");
    }

    public void onMapClicked(javafx.scene.input.MouseEvent event) {
        model.actionMapClicked(event);
        model.redrawMap(mapCanvas.getGraphicsContext2D());
    }

    public void setModel(MainWindowModel model) {
        this.model = model;
    }

    public void setParentStage(Stage primaryStage) {
        parentStage = primaryStage;
    }

    public void closeApplication() {
        Platform.exit();
    }

    public void onSaveMapClicked() {
        model.saveMap(parentStage);
    }

    public void onLoadMapClicked() {
        if (model.actionLoadMap(parentStage))
            model.redrawMap(mapCanvas.getGraphicsContext2D());
    }

    public void onShow() {
        bugList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        model.redrawMap(mapCanvas.getGraphicsContext2D());
        loadBugList();
        bugList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                onBugSelected();
            }
        });
    }

    private void onBugSelected() {
        int bugCount = bugList.getSelectionModel().getSelectedItems().size();
        if (bugCount > model.getPortalCount())
            Alerts.errorBox("The map does not support selected amount of bugs. You need to add more portals.");
    }

    public void loadBugList() {
        bugList.getItems().add("item 1");
        bugList.getItems().add("item 2");
        bugList.getItems().add("item 3");
        bugList.getItems().add("item 4");
    }
}
