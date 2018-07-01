package com.archangel_design.core_wars.model;

import com.archangel_design.core_wars.utils.CellType;
import com.archangel_design.core_wars.utils.Map;
import com.archangel_design.core_wars.utils.MapDrawer;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;

public class MainWindowModel extends AbstractModel {

    private Group layout;
    private MapDrawer mapDrawer = new MapDrawer();
    private Scene scene;
    private Map mainMap = new Map(12, 12);
    private Canvas mainCanvas = null;
    private FileChooser fileChooser = new FileChooser();

    public MainWindowModel() {
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
        mapDrawer.drawMap(mainCanvas.getGraphicsContext2D(), mainMap);

        scene = new Scene(layout, 800, 600);
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
        System.out.println("Event triggered: " + event.getEventType());
    }

    private void actionLoadMap(ActionEvent event) {
        System.out.println("Event triggered: " + event.getEventType());
    }

    private Canvas mapCanvas() {
        if (mainCanvas != null)
            return mainCanvas;

        mainCanvas = new Canvas(400, 600);
        mainCanvas.setLayoutX(100);

        return mainCanvas;
    }
}
