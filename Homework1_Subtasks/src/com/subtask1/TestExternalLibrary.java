package com.subtask1;

import com.simple.logger.*;

public class TestExternalLibrary {
    public static final Logger LOGGER = ConsoleLogger.getLogger(TestExternalLibrary.class);

    public static void main(String[] args) {
        System.out.println("\nSimple logger library usage start");
        LOGGER.info("Hello World using My Library!");
        LOGGER.error("Goodbye World using My Library!");
        System.out.println("Simple logger library usage finish\n");
    }
}