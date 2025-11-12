package com.furniture.store.core;

import com.furniture.store.model.Order;

public interface OrderProducer {
    void submit(Order order) throws InterruptedException;
    void submitReservation(Order order) throws InterruptedException;
    void cancelReservation(Order order);
}

