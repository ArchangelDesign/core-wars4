package com.archangel_design.core_wars.model;

import com.archangel_design.core_wars.controller.MainWindowController;
import com.archangel_design.core_wars.utils.Alerts;
import com.archangel_design.core_wars.utils.CellType;
import com.archangel_design.core_wars.utils.Map;
import com.archangel_design.core_wars.utils.MapRenderer;
import com.archangel_design.core_wars.utils.MapLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class MainWindowModel extends AbstractModel {

    private static final int canvasWidth = 500;
    private static final int canvasHeight = 600;
    private static final int windowHeight = 600;
    private static final int windowWidth = 800;

    private MapRenderer mapRenderer = new MapRenderer();
    private FileChooser fileChooser = new FileChooser();


    private CellType currentTool;

    private void redrawMap(GraphicsContext gc, Map map) {
        gc.clearRect(0, 0, canvasWidth, canvasHeight);
        mapRenderer.drawMap(gc, map);
    }

    @Override
    public Scene getScene(Stage primaryStage) {
        Parent p = null;
        try {
            FXMLLoader loader = new FXMLLoader(new File("layout/MainWindow.fxml").toURI().toURL());
            p = loader.load();
            MainWindowController controller = loader.getController();
            controller.setModel(this);
            controller.setParentStage(primaryStage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Scene(p, 800, 600);
    }

    private void saveMap(Map map) {
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CW Map file", "*.cw4")
        );
        File f = fileChooser.showSaveDialog(null);
        if (f == null)
            return;
        try {
            MapLoader.saveMap(map, f.getAbsolutePath());
        } catch (IOException e) {
            Alerts.errorBox("Could not save file. " + e.getMessage());
        }
    }

    private void actionLoadMap(Map map) {
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CW Map file", "*.cw4")
        );
        File f = fileChooser.showOpenDialog(null);
        if (f == null)
            return;
        try {
            map = MapLoader.loadMap(f.getAbsolutePath());
        } catch (Exception e) {
            Alerts.errorBox("Could not load map. " + e.getMessage());
        }
    }
/*
    private void actionMapClicked(javafx.scene.input.MouseEvent event) {
        double posx = event.getX();
        double posy = event.getY();

        int indexX = mainMap.getIndex(posx);
        int indexY = mainMap.getIndex(posy);

        if (currentTool == null) {
            Alerts.errorBox("You need to select a tool first.");
            return;
        }

        mainMap.setCellType(indexX, indexY, currentTool);
        redrawMap();

        System.out.println(String.format("X: %d | Y: %d", indexX, indexY));
    }
*/

}
