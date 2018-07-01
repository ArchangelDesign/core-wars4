package com.archangel_design.core_wars.model;

import com.archangel_design.core_wars.utils.*;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class MainWindowModel extends AbstractModel {

    private static final int canvasWidth = 400;
    private static final int canvasHeight = 600;

    private Group layout;
    private MapDrawer mapDrawer = new MapDrawer();
    private Scene scene;
    private Stage parentStage;
    private Map mainMap = new Map(12, 12);
    private Canvas mainCanvas = null;
    private FileChooser fileChooser = new FileChooser();

    public MainWindowModel(Stage stage) {
        parentStage = stage;
        layout = new Group();
        mainMap.setCellType(1, 3, CellType.BOMB);
        mainMap.setCellType(3, 3, CellType.BARRIER);
        mainMap.setCellType(3, 5, CellType.TELEPORT);
        mainMap.setCellType(4, 5, CellType.TRAP);

        layout.getChildren().addAll(
                mapCanvas(),
                btn_SaveMap(),
                btn_LoadMap()
        );
        mapDrawer.drawMap(mapCanvas().getGraphicsContext2D(), mainMap);

        scene = new Scene(layout, 800, 600);
    }

    private void redrawMap() {
        mapCanvas().getGraphicsContext2D().clearRect(0, 0, canvasWidth, canvasHeight);
        mapDrawer.drawMap(mapCanvas().getGraphicsContext2D(), mainMap);
    }

    @Override
    public Scene getScene() {
        return scene;
    }

    private Button btn_SaveMap() {
        Button result = new Button("Save Map");
        result.setLayoutX(10);
        result.setLayoutY(10);
        result.setOnAction(this::actionSaveMap);

        return result;
    }

    private Button btn_LoadMap() {
        Button result = new Button("Load Map");
        result.setLayoutX(10);
        result.setLayoutY(50);
        result.setOnAction(this::actionLoadMap);

        return result;
    }

    private void actionSaveMap(ActionEvent event) {
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CW Map file", "*.cw4")
        );
        File f =fileChooser.showSaveDialog(parentStage);
        if (f == null)
            return;
        try {
            MapLoader.saveMap(mainMap, f.getAbsolutePath());
        } catch (IOException e) {
            Alerts.errorBox("Could not save file. " + e.getMessage());
        }
    }

    private void actionLoadMap(ActionEvent event) {
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CW Map file", "*.cw4")
        );
        File f = fileChooser.showOpenDialog(parentStage);
        if (f == null)
            return;
        try {
            mainMap = MapLoader.loadMap(f.getAbsolutePath());
            redrawMap();
        } catch (Exception e) {
            Alerts.errorBox("Could not load map. " + e.getMessage());
        }
    }

    private Canvas mapCanvas() {
        if (mainCanvas != null)
            return mainCanvas;

        mainCanvas = new Canvas(canvasWidth, canvasHeight);
        mainCanvas.setLayoutX(100);

        return mainCanvas;
    }
}
