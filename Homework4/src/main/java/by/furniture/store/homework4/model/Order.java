package by.furniture.store.homework4.model;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Order {
    private static final AtomicInteger idCounter = new AtomicInteger(1);
    private final int orderId;
    private final String customerName;
    private final Map<Product, Integer> items;

    public Order(String customerName, Map<Product, Integer> items) {
        this.orderId = idCounter.getAndIncrement();
        this.customerName = customerName;
        this.items = Map.copyOf(items);
    }

    public int getOrderId() {
        return orderId;
    }

    public Map<Product, Integer> getItems() {
        return items;
    }

    @Override
    public String toString() {
        return "#%d (%s): %s".formatted(orderId, customerName, items);
    }
}
