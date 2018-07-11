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
        if (!methods.containsKey(bug.getCompiler().getCurrentMethod()))
            throw new NoLoopMethodException(String.format("No %s method for bug %s.", bug.getCompiler().getCurrentMethod(), bug.getName()));

        Instruction instruction = bug.getCompiler().getMethods().get(bug.getCompiler().getCurrentMethod()).getNext();
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
            conError(String.format("[%s] Variable %s is not defined.", bug.getName(), variable));
            return "";
        }
        return bug.getCompiler().getVariableValue(variable);
    }

    /**
     * Calls method from bug's local stack
     *
     * @param bug target
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
            case "scanForward":
                scanForward(bug);
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

    private static void scanForward(BugEntity bug) {
        int x = bug.getX();
        int y = bug.getY();

        switch (bug.getDirection()) {
            case RIGHT:
                x++; break;
            case LEFT:
                x--; break;
            case UP:
                y--; break;
            case DOWN:
                y++; break;
        }

        if (x > currentMap.getWidth() || x < 1 ||
                y > currentMap.getHeight() || y < 1) {
            bug.getCompiler().declareVariable("$scanResult", "BARRIER");
            return;
        }

        switch (currentMap.getCell(x, y).getType()) {
            case BARRIER:
                bug.getCompiler().declareVariable("$scanResult", "BARRIER");
                break;
            case EMPTY:
                bug.getCompiler().declareVariable("$scanResult", "EMPTY");
                break;
            case MINE:
                bug.getCompiler().declareVariable("$scanResult", "MINE");
                break;
        }

        // @TODO: detect bugs
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

    private static void conError(String msg) {
        if (console == null)
            throw new RuntimeException(msg);
        console.appendText("ERROR: " + msg + System.getProperty("line.separator"));
    }
}
