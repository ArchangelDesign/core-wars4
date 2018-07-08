package com.archangel_design.core_wars.utils.compiler;

import com.archangel_design.core_wars.utils.Sound;
import com.archangel_design.core_wars.utils.SoundPlayer;
import com.archangel_design.core_wars.utils.bugs.BugEntity;
import com.archangel_design.core_wars.utils.bugs.Direction;
import com.archangel_design.core_wars.utils.compiler.exception.NoLoopMethodException;
import javafx.scene.control.TextArea;

import java.util.HashMap;

public class Executor {

    public static boolean debugMode = true;
    private static TextArea console;

    public static void setDebugMode(boolean debugMode) {
        Executor.debugMode = debugMode;
    }

    public static void setConsole(TextArea console) {
        Executor.console = console;
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
        }
    }

    private static void move(BugEntity bug) {
        SoundPlayer.playSound(Sound.SND_MOVE);
        switch (bug.getDirection()) {
            case LEFT:
                bug.setX(bug.getX()-1);
                break;
            case DOWN:
                conPrint("moving down ");
                bug.setY(bug.getY()+1);
                break;
            case UP:
                bug.setY(bug.getY()-1);
                break;
            case RIGHT:
                bug.setX(bug.getX()+1);
                break;
        }
    }

    private static void turnLeft(BugEntity bug) {
        conPrint(String.format("bug %s is turning left.", bug.getName()));
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

    private static void conPrint(String msg) {
        if (console == null)
            return;
        if (!debugMode)
            return;
        console.appendText(msg + System.getProperty("line.separator"));
    }
}
