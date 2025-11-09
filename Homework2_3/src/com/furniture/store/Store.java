package com.furniture.store;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;

public class Store implements OrderConsumer, OrderProducer {
    private final Map<Product, Integer> warehouse;
    private final BlockingQueue<Order> orderQueue;
    private final List<Order> processedOrders;

    public Store(int queueLength) {
        warehouse = new ConcurrentHashMap<>(Randomizer.getProductsWithQuantity());
        orderQueue = new LinkedBlockingDeque<>(queueLength);
        processedOrders = Collections.synchronizedList(new ArrayList<>());
    }

    public Map<Product, Integer> getWarehouse() {
        return warehouse;
    }

    public BlockingQueue<Order> getOrderQueue() {
        return orderQueue;
    }

    public void processOrder(Order order, String workerName) throws InterruptedException {
        Thread.sleep(Randomizer.processOrderTimeout());
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

    class Analytics {
        public void runAnalytics() {
            System.out.println("\nAnalytics\n----------------------------------");

            long processedOrdersCount = processedOrders.size();
            System.out.println("The total number of success orders: " + processedOrdersCount);

            BigDecimal totalProfit = processedOrders.parallelStream()
                    .flatMap(order -> order.getItems().entrySet().stream())
                    .map(item -> item.getKey().price().multiply(
                            BigDecimal.valueOf(item.getValue())
                    )).reduce(BigDecimal.ZERO, BigDecimal::add);
            System.out.println("The total profit: $" + totalProfit);

            Map<Product, Integer> top3Products = processedOrders.parallelStream()
                    .flatMap(order -> order.getItems().entrySet().stream())
                    .collect(Collectors.toConcurrentMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            Integer::sum
                    ))
                    .entrySet().stream()
                    .sorted(Map.Entry.<Product, Integer>comparingByValue().reversed())
                    .limit(3)
                    .collect(Collectors.toMap(
                            Map.Entry::getKey, Map.Entry::getValue, (a, _) -> a,
                            LinkedHashMap::new)
                    );

            System.out.println("The top 3 best-selling products:");
            System.out.println(top3Products);
        }
    }
}
