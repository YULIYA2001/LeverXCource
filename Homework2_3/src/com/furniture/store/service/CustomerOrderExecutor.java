package com.furniture.store.service;

import com.furniture.store.core.OrderProducer;
import com.furniture.store.model.Customer;
import com.furniture.store.model.Order;
import com.furniture.store.model.Product;
import com.furniture.store.util.Randomizer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerOrderExecutor implements Runnable {
    private final Customer customer;
    private final OrderProducer orderProducer;
    private final List<Product> catalog;

    public CustomerOrderExecutor(Customer customer, OrderProducer orderProducer, List<Product> catalog) {
        this.customer = customer;
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

        orderProducer.submitOrder(new Order(customer.customerName(), items));
    }
}
