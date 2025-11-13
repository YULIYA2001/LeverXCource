package by.furniture.store.homework4.service;

import by.furniture.store.homework4.model.Order;
import by.furniture.store.homework4.model.Product;
import by.furniture.store.homework4.util.Randomizer;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReservationStoreService implements ReservationOrderProducer {
    private final Map<Product, Integer> warehouse;
    private final List<Order> canceledReservations;

    public ReservationStoreService(Map<Product, Integer> warehouse, List<Order> canceledReservations) {
        this.warehouse = warehouse;
        this.canceledReservations = canceledReservations;
    }

    public void cancelReservation(Order order) {
        order.getItems().forEach((product, quantity) ->
                warehouse.computeIfPresent(product, (_, stock) -> stock + quantity));
        System.out.printf("Reservation: Cancelled. %s%n", order);
        canceledReservations.add(order);
    }

    public void takeReservation(Order order) throws InterruptedException {
        Thread.sleep(Randomizer.creationOrderTimeout());
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

}
