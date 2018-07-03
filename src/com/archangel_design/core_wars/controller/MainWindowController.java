package com.archangel_design.core_wars.controller;

import com.archangel_design.core_wars.model.MainWindowModel;
import com.archangel_design.core_wars.utils.CellType;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import javax.swing.text.html.ImageView;

public class MainWindowController {

    @FXML
    Label statusText;

    @FXML
    ImageView imgBomb;

    @FXML
    Canvas mapCanvas;

    MainWindowModel model;

    Stage parentStage;

    private void updateStatusText(String text) {
        statusText.setText(text);
    }

    public void onBombClicked() {
        model.setCurrentTool(CellType.MINE);
        updateStatusText("Current tool: Mine");
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

    public void onShow() {
        model.redrawMap(mapCanvas.getGraphicsContext2D());
    }
}
