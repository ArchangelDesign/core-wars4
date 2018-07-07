package com.archangel_design.core_wars.controller;

import com.archangel_design.core_wars.model.AbstractModel;
import javafx.stage.Stage;

public interface CoreWarsController {

    void onShow();
    void setModel(AbstractModel model);
    void setParentStage(Stage parentStage);
}
