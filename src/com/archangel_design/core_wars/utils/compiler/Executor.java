package com.archangel_design.core_wars.utils.compiler;

import com.archangel_design.core_wars.utils.*;
import com.archangel_design.core_wars.utils.bugs.BugEntity;
import com.archangel_design.core_wars.utils.bugs.Direction;
import com.archangel_design.core_wars.utils.compiler.exception.NoLoopMethodException;
import com.sun.istack.internal.NotNull;
import javafx.scene.control.TextArea;

import java.util.HashMap;

public class Executor {

    public static boolean debugMode = true;
    private static TextArea console;
    private static Map currentMap;

    public static void setDebugMode(boolean debugMode) {
        Executor.debugMode = debugMode;
    }

    public static void setConsole(TextArea console) {
        Executor.console = console;
    }

    public static void setCurrentMap(@NotNull Map map) {
        currentMap = map;
    }

    public static void executeNextInstruction(BugEntity bug) throws NoLoopMethodException {
        HashMap<String, Stack> methods = bug.getCompiler().getMethods();
        if (!methods.containsKey("loop"))
            throw new NoLoopMethodException(String.format("No loop method for bug %s.", bug.getName()));

        Instruction instruction = bug.getCompiler().getMethods().get("loop").getNext();
        switch (instruction.getType()) {
            case METHOD_CALL:
                callMethod(bug, instruction);
                break;
        }
    }

    private static void callMethod(BugEntity bug, Instruction instruction) {
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
            case "scanForward":
                break;
            case "scanLeft":
                break;
            case "scanRight":
                break;
            case "scanBack":
                break;
            case "shoot":
                break;
            case "sendNoise":
                break;
        }
    }

    private static void move(BugEntity bug) {
        SoundPlayer.playSound(Sound.SND_MOVE);
        switch (bug.getDirection()) {
            case LEFT:
                if (!canMoveLeft(bug)) {
                    conPrint(String.format("[%s] bumps from a barrier.", bug.getName()));
                    return;
                }
                bug.setX(bug.getX() - 1);
                conPrint(String.format("[%s] is moving left to [%d:%d].", bug.getName(), bug.getX(), bug.getY()));
                break;
            case DOWN:
                if (!canMoveDown(bug)) {
                    conPrint(String.format("[%s] bumps from a barrier.", bug.getName()));
                    return;
                }
                bug.setY(bug.getY() + 1);
                conPrint(String.format("[%s] is moving down to [%d:%d].", bug.getName(), bug.getX(), bug.getY()));
                break;
            case UP:
                if (!canMoveUp(bug)) {
                    conPrint(String.format("[%s] bumps from a barrier.", bug.getName()));
                    return;
                }
                bug.setY(bug.getY() - 1);
                conPrint(String.format("[%s] is moving up to [%d:%d].", bug.getName(), bug.getX(), bug.getY()));
                break;
            case RIGHT:
                if (!canMoveRight(bug)) {
                    conPrint(String.format("[%s] bumps from a barrier.", bug.getName()));
                    return;
                }
                bug.setX(bug.getX() + 1);
                conPrint(String.format("[%s] is moving right to [%d:%d].", bug.getName(), bug.getX(), bug.getY()));
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

        if (x == 1)
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
        conPrint(String.format("[%s] is turning left.", bug.getName()));
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
        conPrint(String.format("[%s] is turning right.", bug.getName()));
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

    private static void conPrint(String msg) {
        if (console == null)
            return;
        if (!debugMode)
            return;
        console.appendText(msg + System.getProperty("line.separator"));
    }
}
