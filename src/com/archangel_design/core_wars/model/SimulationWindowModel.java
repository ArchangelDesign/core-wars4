package com.archangel_design.core_wars.model;

import com.archangel_design.core_wars.controller.SimulationWindowController;
import com.archangel_design.core_wars.utils.Alerts;
import com.archangel_design.core_wars.utils.Map;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.IOException;

public class SimulationWindowModel extends AbstractModel {

    private Map currentMap;

    private SimulationWindowController mainController;

    @Override
    public Scene getScene(Stage primaryStage) {
        Parent p = null;
        try {
            FXMLLoader loader = new FXMLLoader(new File("layout/SimulationWindow.fxml").toURI().toURL());
            p = loader.load();
            mainController = loader.getController();
            mainController.setModel(this);
            mainController.setParentStage(primaryStage);
        } catch (IOException e) {
            Alerts.errorBox(e.getMessage());
            e.printStackTrace();
        }
        return new Scene(p, 800, 600);
    }

    public void setMap(Map currentMap) {
        this.currentMap = currentMap;
    }

    public Map getCurrentMap() {
        return currentMap;
    }

    public SimulationWindowController getMainController() {
        return mainController;
    }
}
