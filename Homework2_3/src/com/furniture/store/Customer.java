package com.furniture.store;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Customer implements Runnable {
    private final String customerName;
    private final OrderProducer orderProducer;
    private final List<Product> catalog;

    public Customer(String customerName, OrderProducer orderProducer, List<Product> catalog) {
        this.customerName = customerName;
        this.orderProducer = orderProducer;
        this.catalog = new ArrayList<>(catalog);
        Collections.shuffle(this.catalog);
    }

    @Override
    public void run() {
        Map<Product, Integer> items = new HashMap<>();
        for (int i = 0; i < Randomizer.getOrderItemsCount(); i++) {
            Product p = catalog.get(i);
            int quantity = Randomizer.getOrderItemQuantity();
            items.put(p, quantity);
        }

        orderProducer.submitOrder(new Order(customerName, items));
    }
}
