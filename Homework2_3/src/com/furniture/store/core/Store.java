package com.furniture.store.core;

import com.furniture.store.model.Order;
import com.furniture.store.model.Product;
import com.furniture.store.util.Randomizer;

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

        if (!order.isReservation()) {
            ProcessingResult processingResult = null;
            try {
                processingResult = processing(order);

                if (!processingResult.isSuccess) {
                    processingResult.rollbackDeductions();
                    System.out.printf("Process (%s): FAIL - #%d. %s out of stock%n",
                            workerName, order.getOrderId(), processingResult.outOfStock);
                } else {
                    processedSuccessfully(order, workerName);
                }
            } catch (Exception e) {
                if (processingResult != null) {
                    processingResult.rollbackDeductions();
                }
                throw e;
            }
        } else {
            processedSuccessfully(order, workerName);
        }
    }

    private void processedSuccessfully(Order order, String workerName) {
        processedOrders.add(order);
        System.out.printf("Process (%s): SUCCESS - #%d%n", workerName, order.getOrderId());
    }

    public void submit(Order order) throws InterruptedException {
        Thread.sleep(Randomizer.creationOrderTimeout());
        if (order.isReservation()) {
            ProcessingResult processingResult = null;
            try {
                processingResult = processing(order);
                if (!processingResult.isSuccess) {
                    processingResult.rollbackDeductions();
                    System.out.printf("Reservation: FAIL. %s out of stock. %s%n",
                            processingResult.outOfStock, order);
                } else {
                    System.out.printf("Reservation: SUCCESS. %s%n", order);
                }
            } catch (Exception e) {
                if (processingResult != null) {
                    processingResult.rollbackDeductions();
                }
                throw e;
            }
        } else {
            System.out.println("Order: " + order);
            orderQueue.put(order);
        }
    }

    public void cancelReservation(Order order) {
        order.getItems().forEach((product, quantity) ->
                warehouse.computeIfPresent(product, (_, stock) -> stock + quantity));
        System.out.printf("Reservation: Cancelled. %s%n", order);
    }

    public void submitReservation(Order order) throws InterruptedException {
        orderQueue.put(order);
        System.out.printf("Reservation: Submitted. %s%n", order);
    }

    private ProcessingResult processing(Order order) {
        Map<Product, Integer> deductions = new HashMap<>();
        List<Product> outOfStock = new ArrayList<>();

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

        return new ProcessingResult(success, deductions, outOfStock);
    }

    private class ProcessingResult {
        private final boolean isSuccess;
        private final Map<Product, Integer> deductions;
        private final List<Product> outOfStock;

        ProcessingResult(boolean isSuccess, Map<Product, Integer> deductions, List<Product> outOfStock) {
            this.isSuccess = isSuccess;
            this.deductions = deductions;
            this.outOfStock = outOfStock;
        }

        void rollbackDeductions() {
            deductions.forEach((product, quantity) ->
                    warehouse.computeIfPresent(product, (_, stock) -> stock + quantity));
        }
    }

    public class Analytics {
        public void runAnalytics() {
            System.out.println("\nAnalytics\n----------------------------------");

            long processedOrdersCount = processedOrders.size();
            System.out.println("The total number of success orders: " + processedOrdersCount);

            long submittedReservationsCount = processedOrders.parallelStream()
                    .filter(Order::isReservation).count();
            System.out.println("The total number of submitted reservations: " + submittedReservationsCount);

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
