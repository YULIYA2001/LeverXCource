package com.simple.logger;

import java.time.LocalTime;

public abstract class Logger {
    private final String name;

    protected Logger(Class<?> clazz) {
        this.name = clazz.getSimpleName();
    }

    public void info(String message) {
        log("INFO", message);
    }

    public void error(String message) {
        log("ERROR", message);
    }

    protected String formLogString(String level, String message) {
        return String.format("[%s] %s %s: %s", LocalTime.now(), level, name, message);
    }

    protected abstract void log(String level, String message);
}
