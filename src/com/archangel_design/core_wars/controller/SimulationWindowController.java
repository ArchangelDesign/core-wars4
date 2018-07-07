package com.archangel_design.core_wars.controller;

import com.archangel_design.core_wars.model.AbstractModel;
import com.archangel_design.core_wars.model.SimulationWindowModel;
import com.archangel_design.core_wars.utils.Alerts;
import com.archangel_design.core_wars.utils.MapRenderer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class SimulationWindowController implements CoreWarsController {

    @FXML
    TextArea console;

    SimulationWindowModel model;

    Stage parentStage;

    MapRenderer mapRenderer = new MapRenderer();

    @FXML
    Canvas mapCanvas;

    @Override
    public void onShow() {
        mapCanvas.getGraphicsContext2D().clearRect(0, 0, 600, 600);
        conPrint("Starting...");
        loadMap();
    }

    @Override
    public void setModel(AbstractModel model) {
        this.model = (SimulationWindowModel) model;
    }

    @Override
    public void setParentStage(Stage parentStage) {
        this.parentStage = parentStage;
    }

    private void conPrint(String msg) {
        console.appendText(msg + System.getProperty("line.separator"));
    }

    private void loadMap() {
        conPrint("loading map...");
        conPrint(String.format(
                "map width: %d, map height: %d",
                model.getCurrentMap().getWidth(),
                model.getCurrentMap().getHeight())
        );
        mapRenderer.drawMap(mapCanvas.getGraphicsContext2D(), model.getCurrentMap());
    }
}
