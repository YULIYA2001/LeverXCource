package by.furniture.store.homework4.model;

import java.util.Map;

public class ReservedOrder extends Order {
    public ReservedOrder(String customerName, Map<Product, Integer> items) {
        super(customerName, items);
    }

    @Override
    public String toString() {
        return "R " + super.toString();
    }
}
