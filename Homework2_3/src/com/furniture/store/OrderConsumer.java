package com.furniture.store;

public interface OrderConsumer {
    void processOrder(Order order, String workerName) throws InterruptedException;
}