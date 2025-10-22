package com.test;

import com.simple.logger.ConsoleLogger;
import com.simple.logger.Logger;

public class Test {
    public static final Logger LOGGER = ConsoleLogger.getLogger(Test.class);

    public static void main(String[] args) {
        System.out.println("\nSimple logger test start");
        LOGGER.info("Hello World!");
        LOGGER.error("Goodbye World!");
        System.out.println("Simple logger test finish\n");
    }
}