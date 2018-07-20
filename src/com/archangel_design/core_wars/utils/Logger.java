package com.archangel_design.core_wars.utils;

import javafx.scene.control.TextArea;

import java.util.Date;

public class Logger {

    private static TextArea console;

    private static Long timeElapsed = 0L;

    public static void setConsole(TextArea t) {
        console = t;
    }

    public static void reset() {
        timeElapsed = 0L;
    }

    public static void tick() {
        timeElapsed++;
    }

    public static void debug(String msg) {
        if (console != null) {
            console.appendText(String.format("[%d] | %s", timeElapsed, msg));
        }
    }

    public static Long getTime() {
        return timeElapsed;
    }
}
