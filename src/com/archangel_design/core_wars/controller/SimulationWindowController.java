package com.archangel_design.core_wars.controller;

import com.archangel_design.core_wars.model.AbstractModel;
import com.archangel_design.core_wars.model.SimulationWindowModel;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class SimulationWindowController implements CoreWarsController {

    @FXML
    TextArea console;

    SimulationWindowModel model;

    Stage parentStage;

    @FXML
    Canvas mapCanvas;

    @Override
    public void onShow() {

    }

    @Override
    public void setModel(AbstractModel model) {
        this.model = (SimulationWindowModel) model;
    }

    @Override
    public void setParentStage(Stage parentStage) {
        this.parentStage = parentStage;
    }
}
