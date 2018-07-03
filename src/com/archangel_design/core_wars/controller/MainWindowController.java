package com.archangel_design.core_wars.controller;

import com.archangel_design.core_wars.model.MainWindowModel;
import com.archangel_design.core_wars.utils.Alerts;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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

    }

    public void setModel(MainWindowModel model) {
        this.model = model;
    }

    public void setParentStage(Stage primaryStage) {
        parentStage = primaryStage;
    }

    public void closeApplication() {

    }
}
