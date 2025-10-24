package com.simple.logger;

import java.time.LocalTime;

public abstract class Logger {
    private final String name;

    protected Logger(Class<?> clazz) {
        this.name = clazz.getSimpleName();
    }

    public void trace(String message) {
        log(LogLevel.TRACE, message);
    }

    public void debug(String message) {
        log(LogLevel.DEBUG, message);
    }

    public void info(String message) {
        log(LogLevel.INFO, message);
    }

    public void warn(String message) {
        log(LogLevel.WARN, message);
    }

    public void error(String message) {
        log(LogLevel.ERROR, message);
    }

    public void fatal(String message) {
        log(LogLevel.FATAL, message);
    }

    protected String formLogString(LogLevel level, String message) {
        return "[%s] %s %s: %s".formatted(LocalTime.now(), level.toString(), name, message);
    }

    protected abstract void log(LogLevel level, String message);
}
