package com.archangel_design.core_wars.controller;

import com.archangel_design.core_wars.model.AbstractModel;
import com.archangel_design.core_wars.utils.Alerts;
import javafx.stage.Stage;

public class SimulationWindowController implements CoreWarsController {
    @Override
    public void onShow() {
        Alerts.errorBox("");
    }

    @Override
    public void setModel(AbstractModel model) {

    }

    @Override
    public void setParentStage(Stage parentStage) {

    }
}
