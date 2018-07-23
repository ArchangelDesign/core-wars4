package com.archangel_design.core_wars.utils.compiler;

import com.archangel_design.core_wars.utils.*;
import com.archangel_design.core_wars.utils.bugs.BugEntity;
import com.archangel_design.core_wars.utils.bugs.Direction;
import com.archangel_design.core_wars.utils.compiler.exception.NoLoopMethodException;
import javafx.scene.control.TextArea;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

public class Executor {

    public static boolean debugMode = true;
    private static TextArea console;
    private static Map currentMap;
    private static HashMap<String, BugEntity> bugList;
    private static volatile List<Shell> shells;

    public static void setDebugMode(boolean debugMode) {
        Executor.debugMode = debugMode;
    }

    public static void setConsole(TextArea console) {
        Executor.console = console;
    }

    public static void setCurrentMap(Map map) {
        currentMap = map;
    }

    public static void executeNextInstruction(BugEntity bug) throws NoLoopMethodException {
        HashMap<String, Stack> methods = bug.getCompiler().getMethods();
        if (!methods.containsKey(bug.getCompiler().getCurrentMethod()))
            throw new NoLoopMethodException(String.format("No %s method for bug %s.", bug.getCompiler().getCurrentMethod(), bug.getName()));

        Instruction instruction = bug.getCompiler().getMethods().get(bug.getCompiler().getCurrentMethod()).getNext();
        if (instruction == null)
            return;
        switch (instruction.getType()) {
            case METHOD_CALL:
                callMethod(bug, instruction);
                break;
            case CONDITION_START:
                handleCondition(bug, instruction);
                break;
        }
    }

    private static void handleCondition(BugEntity bug, Instruction instruction) {
        String arg1 = evaluateArgument(bug, instruction.getConditionArgument().getArg1());
        String arg2 = evaluateArgument(bug, instruction.getConditionArgument().getArg2());
        String op = instruction.getConditionArgument().getOperator();

        switch (op) {
            case "==":
                if (arg1.equals(arg2))
                    // follow inside
                    return;
                else
                    bug.getCompiler().getMethods().get(bug.getCompiler().getCurrentMethod()).conditionNotMet();
        }
    }

    private static String evaluateArgument(BugEntity bug, String argument) {
        if (argument.contains("$"))
            return getVariableValue(bug, argument);
        return argument;
    }

    private static String getVariableValue(BugEntity bug, String variable) {
        if (!bug.getCompiler().hasVariable(variable)) {
            Logger.error(String.format("[%s] Variable %s is not defined.", bug.getName(), variable));
            return "";
        }
        return bug.getCompiler().getVariableValue(variable);
    }

    /**
     * Calls method from bug's local stack
     *
     * @param bug         target
     * @param instruction current instruction
     */
    private static void callMethod(BugEntity bug, Instruction instruction) {
        if (bug.getCompiler().getMethods().containsKey(instruction.getName())) {
            bug.getCompiler().setCurrentMethod(instruction.getName());
            bug.getCompiler().getCurrentStack().reset();
        }
        switch (instruction.getName()) {
            case "move":
                move(bug);
                break;
            case "turnLeft":
                turnLeft(bug);
                break;
            case "turnRight":
                turnRight(bug);
                break;
            case "turnRandom":
                turnRandom(bug);
                break;
            case "scanForward":
                scanForward(bug);
                break;
            case "longScan":
                longScan(bug);
                break;
            case "scanLeft":
                break;
            case "scanRight":
                break;
            case "scanBack":
                break;
            case "shoot":
                shoot(bug);
                break;
            case "sendNoise":
                break;
            case "debug":
                Logger.debug(evaluateArgument(bug, instruction.getArguments().keySet().stream().findFirst().orElse("NULL")));
                break;
            case "resetDetection":
                resetDetected(bug);
                break;
        }
    }

    private static void shoot(BugEntity bug) {
        SoundPlayer.playSound(Sound.SND_SHOOT);
        shells.add(new Shell((bug.getX() - 1) * 30, (bug.getY() - 1) * 30, bug.getDirection(), bug));
    }

    public static void processShells() {

        synchronized (shells) {
            shells.removeIf(s -> s.getX() < 0 || s.getX() > (currentMap.getWidth() * 30 - 30) ||
                    s.getY() < 0 || s.getY() > (currentMap.getHeight() * 30 - 30));

            shells.forEach(
                    shell -> {
                        switch (shell.getDirection()) {
                            case LEFT:
                                shell.setX(shell.getX() - 6);
                                break;
                            case RIGHT:
                                shell.setX(shell.getX() + 6);
                                break;
                            case UP:
                                shell.setY(shell.getY() - 6);
                                break;
                            case DOWN:
                                shell.setY(shell.getY() + 6);
                                break;
                        }
                    }
            );

            shells.removeIf(shell ->
                currentMap.getCell(
                        currentMap.getPosition(shell.getX()), currentMap.getPosition(shell.getY()))
                        .getType() == CellType.BARRIER
            );

            shells.removeIf(shell -> {
                int shellX = currentMap.getPosition(shell.getX());
                int shellY = currentMap.getPosition(shell.getY());
                for (BugEntity bugEntity : bugList.values()) {
                    if (bugEntity.getX() == shellX && bugEntity.getY() == shellY && !bugEntity.getName().equals(shell.getOwner().getName()) && bugEntity.isAlive()) {
                        Logger.info(String.format("[%s] kills [%s] with a shell.", shell.getOwner().getName(), bugEntity.getName()));
                        kill(bugEntity);
                        return true;
                    }
                }
                return false;
            });
        }
    }

    private static void kill(BugEntity bugEntity) {
        bugEntity.kill();
        SoundPlayer.playSound(Sound.EXPLOSION);
    }

    private static void longScan(BugEntity bug) {
        switch (bug.getDirection()) {
            case DOWN:
                longScanDown(bug);
                break;
            case UP:
                longScanUp(bug);
                break;
            case LEFT:
                longScanLeft(bug);
                break;
            case RIGHT:
                longScanRight(bug);
        }
    }

    private static void longScanLeft(BugEntity bug) {
        if (bug.getX() == 1)
            setScanResult(bug, ScanResult.CLEAR);
        for (int i = bug.getX(); i > 0; i--) {
            if (isBugInPosition(i, bug.getY(), bug)) {
                setScanResult(bug, ScanResult.BUG);
                return;
            }
        }
        setScanResult(bug, ScanResult.CLEAR);
    }

    private static void longScanRight(BugEntity bug) {
        if (bug.getX() == currentMap.getWidth())
            setScanResult(bug, ScanResult.CLEAR);
        for (int i = bug.getX(); i <= currentMap.getWidth(); i++) {
            if (isBugInPosition(i, bug.getY(), bug)) {
                setScanResult(bug, ScanResult.BUG);
                return;
            }
        }
        setScanResult(bug, ScanResult.CLEAR);
    }

    private static void longScanDown(BugEntity bug) {
        if (bug.getY() == currentMap.getHeight())
            setScanResult(bug, ScanResult.CLEAR);
        for (int i = bug.getY(); i <= currentMap.getHeight(); i++) {
            if (isBugInPosition(bug.getX(), i, bug)) {
                setScanResult(bug, ScanResult.BUG);
                return;
            }
        }
        setScanResult(bug, ScanResult.CLEAR);
    }

    private static void longScanUp(BugEntity bug) {
        if (bug.getY() == 1)
            setScanResult(bug, ScanResult.CLEAR);
        for (int i = bug.getY(); i > 0; i--) {
            if (isBugInPosition(bug.getX(), i, bug)) {
                setScanResult(bug, ScanResult.BUG);
                return;
            }
        }
        setScanResult(bug, ScanResult.CLEAR);
    }

    private static boolean isBugInPosition(int x, int y) {
        for (Entry<String, BugEntity> b : bugList.entrySet()) {
            if (b.getValue().getX() == x &&
                    b.getValue().getY() == y)
                return true;
        }
        return false;
    }

    private static boolean isBugInPosition(int x, int y, BugEntity searcher) {
        for (Entry<String, BugEntity> b : bugList.entrySet()) {
            if ((b.getValue().getX() == x &&
                    b.getValue().getY() == y) &&
                    !isSamePosition(x, y, searcher.getX(), searcher.getY()) &&
                    b.getValue().isAlive()) {
                onDetected(b.getValue());
                return true;
            }
        }
        return false;
    }

    public static boolean shouldMatchEnd() {
        int bugsAlive = 0;
        for (BugEntity b : bugList.values()) {
            if (b.isAlive())
                bugsAlive++;
        }
        return (bugsAlive < 2);
    }

    private static void onDetected(BugEntity value) {
        value.getCompiler().declareVariable("$detected", "YES");
    }

    private static void resetDetected(BugEntity bug) {
        bug.getCompiler().undeclareVariable("$detected");
    }

    private static boolean isSamePosition(int x, int y, int x1, int y1) {
        return (x == x1 && y == y1);
    }


    private static void turnRandom(BugEntity bug) {
        Random rand = new Random();
        if (rand.nextInt(2) == 1)
            turnLeft(bug);
        else
            turnRight(bug);
    }

    private static void scanForward(BugEntity bug) {
        int x = bug.getX();
        int y = bug.getY();

        switch (bug.getDirection()) {
            case RIGHT:
                x++;
                break;
            case LEFT:
                x--;
                break;
            case UP:
                y--;
                break;
            case DOWN:
                y++;
                break;
        }

        if (x > currentMap.getWidth() || x < 1 ||
                y > currentMap.getHeight() || y < 1) {
            setScanResult(bug, ScanResult.BARRIER);
            return;
        }

        switch (currentMap.getCell(x, y).getType()) {
            case BARRIER:
                setScanResult(bug, ScanResult.BARRIER);
                break;
            case EMPTY:
            case PORTAL:
                setScanResult(bug, ScanResult.EMPTY);
                break;
            case MINE:
                setScanResult(bug, ScanResult.MINE);
                break;
        }

        // @TODO: detect bugs
    }

    private static void setScanResult(BugEntity bug, ScanResult result) {
        bug.getCompiler().declareVariable("$scanResult", result.toString());
    }

    private static void move(BugEntity bug) {
        SoundPlayer.playSound(Sound.SND_MOVE);
        switch (bug.getDirection()) {
            case LEFT:
                if (!canMoveLeft(bug)) {
                    Logger.debug(String.format("[%s] bumps from a barrier.", bug.getName()));
                    return;
                }
                bug.setX(bug.getX() - 1);
                Logger.debug(String.format("[%s] is moving left to [%d:%d].", bug.getName(), bug.getX(), bug.getY()));
                break;
            case DOWN:
                if (!canMoveDown(bug)) {
                    Logger.debug(String.format("[%s] bumps from a barrier.", bug.getName()));
                    return;
                }
                bug.setY(bug.getY() + 1);
                Logger.debug(String.format("[%s] is moving down to [%d:%d].", bug.getName(), bug.getX(), bug.getY()));
                break;
            case UP:
                if (!canMoveUp(bug)) {
                    Logger.debug(String.format("[%s] bumps from a barrier.", bug.getName()));
                    return;
                }
                bug.setY(bug.getY() - 1);
                Logger.debug(String.format("[%s] is moving up to [%d:%d].", bug.getName(), bug.getX(), bug.getY()));
                break;
            case RIGHT:
                if (!canMoveRight(bug)) {
                    Logger.debug(String.format("[%s] bumps from a barrier.", bug.getName()));
                    return;
                }
                bug.setX(bug.getX() + 1);
                Logger.debug(String.format("[%s] is moving right to [%d:%d].", bug.getName(), bug.getX(), bug.getY()));
                break;
        }
    }

    private static boolean canMoveUp(BugEntity bug) {
        int x = bug.getX();
        int y = bug.getY() - 1;

        if (y < 1)
            return false;

        Cell c = currentMap.getCell(x, y);

        if (c.getType() == CellType.BARRIER)
            return false;

        return true;
    }

    private static boolean canMoveDown(BugEntity bug) {
        int x = bug.getX();
        int y = bug.getY() + 1;

        if (y > currentMap.getHeight())
            return false;

        Cell c = currentMap.getCell(x, y);

        if (c.getType() == CellType.BARRIER)
            return false;

        return true;
    }

    private static boolean canMoveLeft(BugEntity bug) {
        int x = bug.getX() - 1;
        int y = bug.getY();

        if (x == 0)
            return false;

        Cell c = currentMap.getCell(x, y);

        if (c.getType() == CellType.BARRIER)
            return false;

        return true;
    }

    private static boolean canMoveRight(BugEntity bug) {
        int x = bug.getX() + 1;
        int y = bug.getY();

        if (x > currentMap.getWidth())
            return false;

        Cell c = currentMap.getCell(x, y);

        if (c.getType() == CellType.BARRIER)
            return false;

        return true;
    }

    private static void turnLeft(BugEntity bug) {
        Logger.debug(String.format("[%s] is turning left.", bug.getName()));
        switch (bug.getDirection()) {
            case LEFT:
                bug.setDirection(Direction.DOWN);
                break;
            case UP:
                bug.setDirection(Direction.LEFT);
                break;
            case RIGHT:
                bug.setDirection(Direction.UP);
                break;
            case DOWN:
                bug.setDirection(Direction.RIGHT);
                break;
        }
    }

    private static void turnRight(BugEntity bug) {
        Logger.debug(String.format("[%s] is turning right.", bug.getName()));
        switch (bug.getDirection()) {
            case LEFT:
                bug.setDirection(Direction.UP);
                break;
            case UP:
                bug.setDirection(Direction.RIGHT);
                break;
            case RIGHT:
                bug.setDirection(Direction.DOWN);
                break;
            case DOWN:
                bug.setDirection(Direction.LEFT);
                break;
        }
    }

    public static void setBugs(HashMap<String, BugEntity> bugs) {
        bugList = bugs;
    }

    public static void setShells(List<Shell> shells) {
        Executor.shells = shells;
    }
}
