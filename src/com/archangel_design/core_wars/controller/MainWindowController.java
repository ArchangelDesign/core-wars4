package com.archangel_design.core_wars.controller;

import com.archangel_design.core_wars.model.AbstractModel;
import com.archangel_design.core_wars.model.MainWindowModel;
import com.archangel_design.core_wars.model.SimulationWindowModel;
import com.archangel_design.core_wars.utils.Alerts;
import com.archangel_design.core_wars.utils.CellType;
import com.archangel_design.core_wars.utils.Map;
import com.archangel_design.core_wars.utils.MapLoader;
import com.archangel_design.core_wars.utils.bugs.BugLoader;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.swing.text.html.ImageView;
import java.io.IOException;
import java.util.HashMap;

public class MainWindowController implements CoreWarsController {

    @FXML
    Label statusText;

    @FXML
    ImageView imgBomb;

    @FXML
    Canvas mapCanvas;

    @FXML
    ListView<String> bugList;

    @FXML
    ListView<String> mapList;

    @FXML
    TextField mapWidthBox;

    @FXML
    TextField mapHeightBox;

    HashMap<String, String> bugRepository;

    MainWindowModel model;

    Stage parentStage;

    Stage simulationWindow = new Stage();

    SimulationWindowModel simulationModel = null;

    private void updateStatusText(String text) {
        statusText.setText(text);
    }

    public void onBombClicked() {
        model.setCurrentTool(CellType.MINE);
        updateStatusText("Current tool: Mine");
    }

    public void onBarrierClicked() {
        model.setCurrentTool(CellType.BARRIER);
        updateStatusText("Current tool: Barrier");
    }

    public void onEmptyClicked() {
        model.setCurrentTool(CellType.EMPTY);
        updateStatusText("Current tool: Empty");
    }

    public void onPortalClicked() {
        model.setCurrentTool(CellType.PORTAL);
        updateStatusText("Current tool: Portal");
    }

    public void onMapClicked(javafx.scene.input.MouseEvent event) {
        model.actionMapClicked(event);
        model.redrawMap(mapCanvas.getGraphicsContext2D());
    }

    public void setModel(AbstractModel model) {
        this.model = (MainWindowModel) model;
    }

    public void setParentStage(Stage primaryStage) {
        parentStage = primaryStage;
    }

    public void closeApplication() {
        Platform.exit();
    }

    public void onSaveMapClicked() {
        model.saveMap(parentStage);
        loadMapList();
    }

    public void onLoadMapClicked() {
        if (model.actionLoadMap(parentStage))
            model.redrawMap(mapCanvas.getGraphicsContext2D());
    }

    public void onShow() {
        bugList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        loadBugList();
        loadMapList();
        bugList.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> onBugSelected()
        );
        mapList.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> onMapSelected(newValue)
        );

        model.addBug(1, 1,
                bugRepository.entrySet().stream().findFirst().get().getKey(),
                bugRepository.entrySet().stream().findFirst().get().getValue()
        );
        model.redrawMap(mapCanvas.getGraphicsContext2D());

        updateDimetionBoxes();
        mapWidthBox.textProperty().addListener(
                (observable, oldValue, newValue) -> onMapWidthChange(newValue)
        );

        mapHeightBox.textProperty().addListener(
                (observable, oldValue, newValue) -> onMapHeightChange(newValue)
        );
    }

    private void updateDimetionBoxes() {
        mapWidthBox.setText(model.getCurrentMap().getWidth().toString());
        mapHeightBox.setText(model.getCurrentMap().getHeight().toString());
    }

    private void loadMapList() {
        mapList.getItems().clear();
        mapList.getItems().addAll(MapLoader.getMapList());
    }

    private void onBugSelected() {
        int bugCount = bugList.getSelectionModel().getSelectedItems().size();
        if (bugCount > model.getPortalCount())
            Alerts.errorBox("The map does not support selected amount of bugs. You need to add more portals.");
    }

    public void onMapSelected(String selectedMap) {
        if (selectedMap == null)
            return;

        try {
            model.clearMap(mapCanvas.getGraphicsContext2D());
            model.loadMap(MapLoader.loadMap(MapLoader.getFullPath(selectedMap)));
        } catch (IOException e) {
            Alerts.errorBox("Couldn't load map " + selectedMap);
            return;
        }
        model.redrawMap(mapCanvas.getGraphicsContext2D());
        updateDimetionBoxes();
    }

    void onMapWidthChange(String newWidth) {
        if (newWidth.isEmpty())
            return;
        int w = Integer.parseInt(newWidth);
        if (!model.getCurrentMap().getWidth().equals(w)) {
            int currentHeight = model.getCurrentMap().getHeight();
            Map m = new Map(w, currentHeight);
            model.loadMap(m);
            model.redrawMap(mapCanvas.getGraphicsContext2D());
        }
    }

    void onMapHeightChange(String newHeight) {
        if (newHeight.isEmpty())
            return;
        int h = Integer.parseInt(newHeight);
        if (!model.getCurrentMap().getHeight().equals(h)) {
            int currentWidtth = model.getCurrentMap().getWidth();
            Map m = new Map(currentWidtth, h);
            model.loadMap(m);
            model.redrawMap(mapCanvas.getGraphicsContext2D());
        }
    }

    public void onStartSimulationClicked() {
        if (simulationModel == null) {
            simulationModel = new SimulationWindowModel();
            simulationWindow.initOwner(parentStage);
            simulationWindow.initModality(Modality.WINDOW_MODAL);
            simulationWindow.setScene(simulationModel.getScene(parentStage));
            simulationWindow.addEventFilter(
                    WindowEvent.WINDOW_SHOWN,
                    event -> simulationModel.getMainController().onShow()
            );
        }
        simulationModel.setMap(model.getCurrentMap());
        simulationWindow.showAndWait();
    }

    public void loadBugList() {
        bugRepository = BugLoader.loadBugs();
        bugRepository.forEach(
                (key, value) -> bugList.getItems().add(key)
        );
    }
}
