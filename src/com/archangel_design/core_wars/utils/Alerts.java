package com.archangel_design.core_wars.utils;

import javafx.scene.control.Alert;

public class Alerts {

    public static void errorBox(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Core Wars 4");
        a.setContentText(msg);
        a.showAndWait();
    }
}
