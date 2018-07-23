package com.archangel_design.core_wars.controller;

import com.archangel_design.core_wars.model.AbstractModel;
import com.archangel_design.core_wars.model.SimulationWindowModel;
import com.archangel_design.core_wars.utils.*;
import com.archangel_design.core_wars.utils.bugs.BugEntity;
import com.archangel_design.core_wars.utils.bugs.BugLoader;
import com.archangel_design.core_wars.utils.bugs.Direction;
import com.archangel_design.core_wars.utils.compiler.Executor;
import com.archangel_design.core_wars.utils.compiler.Parser;
import com.archangel_design.core_wars.utils.compiler.Stack;
import com.archangel_design.core_wars.utils.compiler.exception.NoLoopMethodException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Dimension2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SimulationWindowController implements CoreWarsController {

    @FXML
    TextArea console;

    @FXML
    Label timeLabel;

    @FXML
    Label cyclesLabel;

    SimulationWindowModel model;

    Stage parentStage;

    MapRenderer mapRenderer = new MapRenderer();

    HashMap<String, BugEntity> bugs;

    private long maxCycles = 0;

    private long cycles = 0;

    private boolean running = true;

    private int currentPortal;

    @FXML
    Canvas mapCanvas;

    private volatile List<Shell> shells = new ArrayList<>();

    @Override
    public void onShow() {
        cycles = 0;
        Logger.setConsole(console);
        mapCanvas.getGraphicsContext2D().clearRect(0, 0, 600, 600);
        Logger.info("Starting...");
        loadMap();
        loadBugs();
        compileBugs();
        Logger.info("Ready for simulation.");
        SoundPlayer.playSound(Sound.SND_OPENING);
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
        running = false;
    }

    private void startSimulation() {
        Logger.info("Staring simulation...");
        Logger.reset();
        running = true;
        Executor.setConsole(console);
        Executor.setCurrentMap(model.getCurrentMap());
        Executor.setBugs(bugs);
        Executor.setShells(shells);
        SoundPlayer.playSound(Sound.SND_BUZZER);
        new Thread(this::sceneUpdate).start();
        new Thread(this::timeTick).start();
        new Thread(this::processShells).start();

        while (running) {
            bugs.forEach((s, bugEntity) -> {
                try {
                    if (bugEntity.isAlive())
                        Executor.executeNextInstruction(bugEntity);
                } catch (NoLoopMethodException e) {
                    Logger.error(e.getMessage());
                }
            });

            cycles++;

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (cycles > maxCycles && maxCycles > 0) {
                running = false;
                Logger.info("Simulation terminated due to reach of max cycles.");
            }

            if (Executor.shouldMatchEnd()) {
                running = false;
                Platform.runLater(() -> {
                    mapRenderer.redrawMap(mapCanvas.getGraphicsContext2D(), model.getCurrentMap());
                    mapRenderer.drawBugs(bugs, mapCanvas.getGraphicsContext2D());
                });

                Logger.info("Match ended.");
                BugEntity winner = getWinner();
                if (winner == null) {
                    Logger.error("Could not find a winner.");
                    return;
                }
                Logger.info(String.format("Winner: %s with %d kills.", winner.getName(), 0));
            }
        }

        Logger.info("Simulation ended.");

    }

    private BugEntity getWinner() {
        for (BugEntity b : bugs.values()) {
            if (b.isAlive())
                return b;
        }
        return null;
    }

    private void timeTick() {
        while (running) {
            Logger.tick();
            Platform.runLater(() -> {
                        timeLabel.setText(
                                String.format("Time elapsed: %d", Logger.getTime())
                        );
                        cyclesLabel.setText(String.format("Cycles: %d", cycles));
                    }
            );
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void processShells() {
        while (running) {
            Executor.processShells();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void sceneUpdate() {
        while (running) {
            Platform.runLater(() -> {
                mapRenderer.redrawMap(mapCanvas.getGraphicsContext2D(), model.getCurrentMap());
                mapRenderer.drawBugs(bugs, mapCanvas.getGraphicsContext2D());
                mapRenderer.drawBullets(shells, mapCanvas.getGraphicsContext2D());
            });
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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
        Logger.info("loading map...");
        Logger.debug(String.format(
                "map width: %d, map height: %d",
                model.getCurrentMap().getWidth(),
                model.getCurrentMap().getHeight())
        );
        mapRenderer.drawMap(mapCanvas.getGraphicsContext2D(), model.getCurrentMap());
    }

    private void loadBugs() {
        Logger.info(String.format("loading %d bugs...", model.getBugList().size()));
        bugs = new HashMap<>();

        model.getBugList().forEach(
                b -> {
                    conPrint(String.format("loading %s ...", b.toString()));
                    BugEntity bug = BugLoader.loadBug(b.toString());
                    bugs.put(bug.getName(), bug);
                }
        );

        List<Dimension2D> portals = model.getCurrentMap().getPortals();

        Logger.debug("placing bugs on map...");

        currentPortal = 0;

        bugs.forEach((s, bugEntity) -> {
            int x = (int) portals.get(currentPortal).getWidth();
            int y = (int) portals.get(currentPortal).getHeight();
            Logger.debug(String.format("placing %s on %dx%d", s, x, y));
            bugEntity.setX(x)
                    .setY(y);
            currentPortal++;
        });

        mapRenderer.drawBugs(bugs, mapCanvas.getGraphicsContext2D());
    }

    private void compileBugs() {
        Logger.info("Compiling bugs...");
        bugs.forEach((bugName, bugEntity) -> {
                    Parser.loadMethods(bugEntity.getPath()).forEach(
                            (name, body) -> bugEntity.addMethod(name, Parser.readStack(body)));
                    conPrint(String.format("loaded %d functions for %s.", bugEntity.getMethodCount(), bugName));
                }
        );
        Logger.info("bugs compiled.");
    }
}
