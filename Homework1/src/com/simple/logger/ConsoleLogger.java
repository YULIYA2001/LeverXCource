package com.simple.logger;

public class ConsoleLogger extends Logger {
    private ConsoleLogger(Class<?> clazz) {
        super(clazz);
    }

    @Override
    protected void log(String level, String message) {
        System.out.println(formLogString(level, message));
    }

    public static Logger getLogger(Class<?> clazz){
        return new ConsoleLogger(clazz);
    };
}
