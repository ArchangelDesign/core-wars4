package com.archangel_design.core_wars.model;

import com.archangel_design.core_wars.controller.MainWindowController;
import com.archangel_design.core_wars.utils.Alerts;
import com.archangel_design.core_wars.utils.CellType;
import com.archangel_design.core_wars.utils.Map;
import com.archangel_design.core_wars.utils.MapRenderer;
import com.archangel_design.core_wars.utils.MapLoader;
import com.archangel_design.core_wars.utils.bugs.BugEntity;
import com.archangel_design.core_wars.utils.bugs.Direction;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainWindowModel extends AbstractModel {

    private static final int canvasWidth = 500;
    private static final int canvasHeight = 600;
    private static final int windowHeight = 600;
    private static final int windowWidth = 800;

    private MapRenderer mapRenderer = new MapRenderer();
    private FileChooser fileChooser = new FileChooser();

    private CellType currentTool;
    private Map currentMap = new Map(10, 10);
    private List<BugEntity> bugs = new ArrayList<>();

    public void redrawMap(GraphicsContext gc) {
        gc.clearRect(0, 0, canvasWidth, canvasHeight);
        mapRenderer.drawMap(gc, currentMap);
        bugs.forEach(b -> mapRenderer.drawBug(
                currentMap.getCell(b.getX(), b.getY()).getRealPositionX(30),
                currentMap.getCell(b.getX(), b.getY()).getRealPositionY(30),
                1,
                30,
                b.getDirection(),
                gc
        ));
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
            primaryStage.addEventHandler(WindowEvent.WINDOW_SHOWN, event -> controller.onShow());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Scene(p, 800, 600);
    }

    public void saveMap(Stage parent) {
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CW Map file", "*.cw4")
        );
        File f = fileChooser.showSaveDialog(parent);
        if (f == null)
            return;
        try {
            MapLoader.saveMap(currentMap, f.getAbsolutePath());
        } catch (IOException e) {
            Alerts.errorBox("Could not save file. " + e.getMessage());
        }
    }

    public boolean actionLoadMap(Stage parent) {
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CW Map file", "*.cw4")
        );
        File f = fileChooser.showOpenDialog(parent);
        if (f == null)
            return false;
        try {
            currentMap = MapLoader.loadMap(f.getAbsolutePath());
            return true;
        } catch (Exception e) {
            Alerts.errorBox("Could not load map. " + e.getMessage());
            return false;
        }
    }

    public void setCurrentTool(CellType cellType) {
        currentTool = cellType;
    }

    public void actionMapClicked(javafx.scene.input.MouseEvent event) {
        double posx = event.getX();
        double posy = event.getY();

        int indexX = currentMap.getIndex(posx);
        int indexY = currentMap.getIndex(posy);

        if (currentTool == null) {
            Alerts.errorBox("You need to select a tool first.");
            return;
        }

        currentMap.setCellType(indexX, indexY, currentTool);
    }

    public int getPortalCount() {
        return currentMap.getPortalCount();
    }

    public void addBug(int x, int y, String name, String path) {
        BugEntity e = new BugEntity();
        e.setName(name)
                .setDirection(Direction.DOWN)
                .setX(x)
                .setY(y)
                .setPath(path);
        bugs.add(e);
    }

    public void loadMap(Map theMap) {
        this.currentMap = theMap;
    }
}
