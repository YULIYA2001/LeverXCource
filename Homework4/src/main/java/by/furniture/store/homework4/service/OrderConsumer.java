package by.furniture.store.homework4.service;

import by.furniture.store.homework4.model.Order;

public interface OrderConsumer {
    void processOrder(Order order, String workerName) throws InterruptedException;
}
