package com.archangel_design.core_wars.utils;

import javafx.scene.control.TextArea;

public class Logger {

    private enum Level {DEBUG, INFO, WARNING, ERROR, FATAL}

    private static TextArea console;

    private static Level level = Level.INFO;

    private static Long timeElapsed = 0L;

    public static void setConsole(TextArea t) {
        console = t;
    }

    public static void reset() {
        timeElapsed = 0L;
        if (console != null)
            console.clear();
    }

    public static void tick() {
        timeElapsed++;
    }

    public static void debug(String msg) {
        if (level == Level.DEBUG)
            output(msg, Level.DEBUG);
    }

    public static Long getTime() {
        return timeElapsed;
    }

    public static void info(String msg) {
        output(msg, Level.INFO);
    }

    public static void error(String msg) {
        output(msg, Level.ERROR);
    }

    public static void fatal(String msg) {

    }

    private static void output(String msg, Level messageLevel) {
        switch (messageLevel) {
            case INFO:
                outputInfo(msg);
                break;
            case DEBUG:
                outputDebug(msg);
                break;
            case ERROR:
                outputError(msg);
                break;
        }
    }

    private static void outputInfo(String msg) {
        String message = String.format("[%d] | %s", timeElapsed, msg);
        outputToConsole(message);
    }

    private static void outputError(String msg) {
        String message = String.format("ERROR: [%d] | %s", timeElapsed, msg);
        outputToConsole(message);
    }

    private static void outputDebug(String msg) {
        String message = String.format("DEBUG: [%d] | %s", timeElapsed, msg);
        outputToConsole(message);
    }


    private static void outputToConsole(String msg) {
        if (console != null)
            console.appendText(msg + System.getProperty("line.separator"));
    }

    private static void outputToFile(String msg) {
        // @TODO
    }
}
