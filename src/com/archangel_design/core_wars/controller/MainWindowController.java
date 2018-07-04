package com.archangel_design.core_wars.controller;

import com.archangel_design.core_wars.model.MainWindowModel;
import com.archangel_design.core_wars.utils.Alerts;
import com.archangel_design.core_wars.utils.CellType;
import com.archangel_design.core_wars.utils.bugs.BugLoader;
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
import java.util.HashMap;

public class MainWindowController {

    @FXML
    Label statusText;

    @FXML
    ImageView imgBomb;

    @FXML
    Canvas mapCanvas;

    @FXML
    ListView<String> bugList;

    HashMap<String, String> bugRepository;

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
        bugList.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> onBugSelected()
        );

        model.addBug(1, 1,
                bugRepository.entrySet().stream().findFirst().get().getKey(),
                bugRepository.entrySet().stream().findFirst().get().getValue()
        );
        model.redrawMap(mapCanvas.getGraphicsContext2D());
    }

    private void onBugSelected() {
        int bugCount = bugList.getSelectionModel().getSelectedItems().size();
        if (bugCount > model.getPortalCount())
            Alerts.errorBox("The map does not support selected amount of bugs. You need to add more portals.");
    }

    public void loadBugList() {
        bugRepository = BugLoader.loadBugs();
        bugRepository.forEach(
                (key, value) -> bugList.getItems().add(key)
        );
    }
}
