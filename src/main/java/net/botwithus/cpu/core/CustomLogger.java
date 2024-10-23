package net.botwithus.cpu.core;

import net.botwithus.rs3.script.Script;

public class CustomLogger {
    private static Script script;
    private static boolean debug = true;

    public static void setScript(Script script) {
        CustomLogger.script = script;
    }

    public static void log(String message) {
        if (debug) {
            script.println(message);
        }
    }

    public static void logThrowable(Throwable e) {
        log("Exception thrown by: " + Thread.currentThread().getStackTrace()[2].getClassName());
        log(e.getMessage());
        for (var element : e.getStackTrace())
            log(element.toString());
        var cause = e.getCause();
        if (cause != null) {
            log("Caused by: ");
            logThrowable(cause);
        }
    }
}
