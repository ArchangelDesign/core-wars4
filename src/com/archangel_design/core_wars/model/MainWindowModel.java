package com.archangel_design.core_wars.model;

import com.archangel_design.core_wars.utils.Map;
import com.archangel_design.core_wars.utils.MapDrawer;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;

public class MainWindowModel extends AbstractModel {

    private Group layout;
    private MapDrawer mapDrawer = new MapDrawer();
    private Scene scene;
    private Canvas mainCanvas = new Canvas(400, 600);

    public MainWindowModel() {
        layout = new Group();
        mapDrawer.drawMap(mainCanvas.getGraphicsContext2D(), new Map(10, 10));

        layout.getChildren().add(mainCanvas);
        scene = new Scene(layout, 800, 600);
    }

    @Override
    public Scene getScene() {
        return scene;
    }
}
