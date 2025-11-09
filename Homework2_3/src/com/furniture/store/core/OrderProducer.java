package com.furniture.store.core;

import com.furniture.store.model.Order;

public interface OrderProducer {
    void submitOrder(Order order);
}
