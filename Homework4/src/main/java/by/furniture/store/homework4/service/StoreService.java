package by.furniture.store.homework4.service;

import by.furniture.store.homework4.model.Order;
import by.furniture.store.homework4.model.Product;
import by.furniture.store.homework4.model.ReservedOrder;
import by.furniture.store.homework4.util.Randomizer;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

@Service
public class StoreService implements OrderConsumer, OrderProducer {
    private final Map<Product, Integer> warehouse;
    private final BlockingQueue<Order> orderQueue;
    private final List<Order> processedOrders;

    public StoreService(Map<Product, Integer> warehouse,
                        BlockingQueue<Order> orderQueue,
                        List<Order> processedOrders) {
        this.warehouse = warehouse;
        this.orderQueue = orderQueue;
        this.processedOrders = processedOrders;
    }

    public void processOrder(Order order, String workerName) throws InterruptedException {
        Thread.sleep(Randomizer.processOrderTimeout());

        if (order instanceof ReservedOrder) {
            processedOrders.add(order);
            System.out.printf("Process (%s): SUCCESS - #%d%n", workerName, order.getOrderId());
            return;
        }

        Map<Product, Integer> deductions = new HashMap<>();
        List<Product> outOfStock = new ArrayList<>();

        try {
            boolean success = order.getItems().entrySet().stream()
                    .allMatch(item -> warehouse.computeIfPresent(
                            item.getKey(), (product, currentStock) -> {
                                if (currentStock >= item.getValue()) {
                                    deductions.put(product, item.getValue());
                                    return currentStock - item.getValue();
                                }
                                outOfStock.add(product);
                                return currentStock;
                            }) != null && deductions.containsKey(item.getKey()));

            if (!success) {
                rollbackDeductions(deductions);
                System.out.printf("Process (%s): FAIL - #%d. %s out of stock%n",
                        workerName, order.getOrderId(), outOfStock);
            } else {
                processedOrders.add(order);
                System.out.printf("Process (%s): SUCCESS - #%d%n", workerName, order.getOrderId());
            }
        } catch (Exception e) {
            rollbackDeductions(deductions);
            throw e;
        }
    }

    private void rollbackDeductions(Map<Product, Integer> deductions) {
        deductions.forEach((product, quantity) ->
                warehouse.computeIfPresent(product, (_, stock) -> stock + quantity)
        );
    }

    public void submitOrder(Order order) {
        try {
            Thread.sleep(Randomizer.creationOrderTimeout());
            System.out.println("Submitted: " + order);
            orderQueue.put(order);
        } catch (InterruptedException _) {
            Thread.currentThread().interrupt();
        }
    }
}
