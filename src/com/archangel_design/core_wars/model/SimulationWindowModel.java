package com.archangel_design.core_wars.model;

import com.archangel_design.core_wars.controller.MainWindowController;
import com.archangel_design.core_wars.controller.SimulationWindowController;
import com.archangel_design.core_wars.utils.Alerts;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.IOException;

public class SimulationWindowModel extends AbstractModel {

    @Override
    public Scene getScene(Stage primaryStage) {
        Parent p = null;
        try {
            FXMLLoader loader = new FXMLLoader(new File("layout/SimulationWindow.fxml").toURI().toURL());
            p = loader.load();
            SimulationWindowController controller = loader.getController();
            controller.setModel(this);
            controller.setParentStage(primaryStage);
            primaryStage.addEventHandler(WindowEvent.WINDOW_SHOWN, event -> controller.onShow());
        } catch (IOException e) {
            Alerts.errorBox(e.getMessage());
            e.printStackTrace();
        }
        return new Scene(p, 800, 600);
    }
}
