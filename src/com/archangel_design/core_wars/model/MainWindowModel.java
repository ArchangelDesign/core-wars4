package com.archangel_design.core_wars.model;

import com.archangel_design.core_wars.utils.Alerts;
import com.archangel_design.core_wars.utils.CellType;
import com.archangel_design.core_wars.utils.Map;
import com.archangel_design.core_wars.utils.MapDrawer;
import com.archangel_design.core_wars.utils.MapLoader;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class MainWindowModel extends AbstractModel {

    private static final int canvasWidth = 400;
    private static final int canvasHeight = 600;
    private static final int windowHeight = 600;
    private static final int windowWidth = 800;

    private Group layout;
    private MapDrawer mapDrawer = new MapDrawer();
    private Scene scene;
    private Stage parentStage;
    private Map mainMap = new Map(12, 12);
    private Canvas mainCanvas = null;
    private FileChooser fileChooser = new FileChooser();
    private Label statusLabel = null;
    private String statusText = "Core Wars 4";

    private CellType currentTool;

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
                btn_LoadMap(),
                mapToolbar(),
                statusLabel()
        );
        mapDrawer.drawMap(mapCanvas().getGraphicsContext2D(), mainMap);

        scene = new Scene(layout, windowWidth, windowHeight);
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
        result.setLayoutY(50);
        result.setOnAction(this::actionSaveMap);

        return result;
    }

    private Button btn_LoadMap() {
        Button result = new Button("Load Map");
        result.setLayoutX(10);
        result.setLayoutY(100);
        result.setOnAction(this::actionLoadMap);

        return result;
    }

    private Canvas mapCanvas() {
        if (mainCanvas != null)
            return mainCanvas;

        mainCanvas = new Canvas(canvasWidth, canvasHeight);
        mainCanvas.setLayoutX(100);
        mainCanvas.setLayoutY(50);

        mainCanvas.setOnMouseClicked(this::actionMapClicked);
        return mainCanvas;
    }

    private Label statusLabel() {
        if (statusLabel != null)
            return statusLabel;
        Label l = new Label("Core Wars 4");
        l.setLayoutX(5);
        l.setLayoutY(580);
        statusLabel = l;

        return l;
    }

    private void setStatusText(String txt) {
        statusLabel.setText(txt);
    }

    private ToolBar mapToolbar() {
        Button btn_Empty = new Button("EMPTY");
        btn_Empty.setOnAction(event -> {
            currentTool = CellType.EMPTY;
            updateStatusWithCurrentTool();
        });

        Button btn_barrier = new Button("BARRIER");
        btn_barrier.setOnAction(event -> {
            currentTool = CellType.BARRIER;
            updateStatusWithCurrentTool();
        });

        Button btn_bomb = new Button("BOMB");
        btn_bomb.setOnAction(event -> {
            currentTool = CellType.BOMB;
            updateStatusWithCurrentTool();
        });

        Button btn_trap = new Button("TRAP");
        btn_trap.setOnAction(event -> {
            currentTool = CellType.TRAP;
            updateStatusWithCurrentTool();
        });

        Button btn_teleport = new Button("TELEPORT");
        btn_teleport.setOnAction(event -> {
            currentTool = CellType.TELEPORT;
            updateStatusWithCurrentTool();
        });

        ToolBar tb = new ToolBar(
                btn_Empty,
                btn_barrier,
                btn_bomb,
                btn_trap,
                btn_teleport
        );


        return tb;
    }

    private void updateStatusWithCurrentTool() {
        setStatusText("Selected tool: " + currentTool.toString());
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

    private void actionMapClicked(javafx.scene.input.MouseEvent event) {
        double posx = event.getX();
        double posy = event.getY();

        int indexX = mainMap.getIndex(posx);
        int indexY = mainMap.getIndex(posy);

        mainMap.setCellType(indexX, indexY, currentTool);
        redrawMap();

        System.out.println(String.format("X: %d | Y: %d", indexX, indexY));
    }


}
