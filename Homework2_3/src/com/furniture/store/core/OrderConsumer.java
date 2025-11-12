package com.furniture.store.core;

import com.furniture.store.model.Order;

public interface OrderConsumer {
    void processOrder(Order order, String workerName) throws InterruptedException;
}