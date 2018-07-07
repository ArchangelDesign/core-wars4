package com.archangel_design.core_wars.controller;

import com.archangel_design.core_wars.model.AbstractModel;
import com.archangel_design.core_wars.model.SimulationWindowModel;
import com.archangel_design.core_wars.utils.MapRenderer;
import com.archangel_design.core_wars.utils.Sound;
import com.archangel_design.core_wars.utils.SoundPlayer;
import com.archangel_design.core_wars.utils.bugs.BugEntity;
import com.archangel_design.core_wars.utils.bugs.BugLoader;
import com.archangel_design.core_wars.utils.compiler.Parser;
import com.archangel_design.core_wars.utils.compiler.Stack;
import javafx.fxml.FXML;
import javafx.geometry.Dimension2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.List;

public class SimulationWindowController implements CoreWarsController {

    @FXML
    TextArea console;

    SimulationWindowModel model;

    Stage parentStage;

    MapRenderer mapRenderer = new MapRenderer();

    HashMap<String, BugEntity> bugs;

    private int currentPortal;

    @FXML
    Canvas mapCanvas;

    @Override
    public void onShow() {
        mapCanvas.getGraphicsContext2D().clearRect(0, 0, 600, 600);
        conPrint("Starting...");
        loadMap();
        loadBugs();
        compileBugs();
        conPrint("Ready for simulation.");
        SoundPlayer.playSound(Sound.SND_OPENING);
        SoundPlayer.playAmbience(Sound.AMB_NATURE);
        new Thread(() -> {
            try {
                Thread.sleep(2300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            startSimulation();
        }).start();
    }

    @Override
    public void onClose() {
        SoundPlayer.stopAmbience();
    }

    private void startSimulation() {
        conPrint("Staring simulation...");
        SoundPlayer.playSound(Sound.SND_BUZZER);
    }

    @Override
    public void setModel(AbstractModel model) {
        this.model = (SimulationWindowModel) model;
    }

    @Override
    public void setParentStage(Stage parentStage) {
        this.parentStage = parentStage;
    }

    private void conPrint(String msg) {
        console.appendText(msg + System.getProperty("line.separator"));
    }

    private void loadMap() {
        conPrint("loading map...");
        conPrint(String.format(
                "map width: %d, map height: %d",
                model.getCurrentMap().getWidth(),
                model.getCurrentMap().getHeight())
        );
        mapRenderer.drawMap(mapCanvas.getGraphicsContext2D(), model.getCurrentMap());
    }

    private void loadBugs() {
        conPrint(String.format("loading %d bugs...", model.getBugList().size()));
        bugs = new HashMap<>();

        model.getBugList().forEach(
                b -> {
                    conPrint(String.format("loading %s ...", b.toString()));
                    BugEntity bug = BugLoader.loadBug(b.toString());
                    bugs.put(bug.getName(), bug);
                }
        );

        List<Dimension2D> portals = model.getCurrentMap().getPortals();

        conPrint("placing bugs on map...");

        currentPortal = 1;

        bugs.forEach((s, bugEntity) -> {
            int x = (int) portals.get(currentPortal).getWidth();
            int y = (int) portals.get(currentPortal).getHeight();
            conPrint(String.format("placing %s on %dx%d", s, x, y));
            bugEntity.setX(x)
                    .setY(y);
            currentPortal++;
        });

        mapRenderer.drawBugs(bugs, mapCanvas.getGraphicsContext2D());
    }

    private void compileBugs() {
        conPrint("Compiling bugs...");
        bugs.forEach((bugName, bugEntity) -> {
                    Parser.loadMethods(bugEntity.getPath()).forEach(
                            (name, body) -> bugEntity.addMethod(name, Parser.readStack(body)));
                    conPrint(String.format("loaded %d functions for %s.", bugEntity.getMethodCount(), bugName));
                }
        );
    }
}
