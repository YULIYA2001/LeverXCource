package by.furniture.store.homework4.service;

import by.furniture.store.homework4.model.Order;
import by.furniture.store.homework4.model.Product;
import by.furniture.store.homework4.util.Randomizer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ReservationStoreService implements ReservationOrderProducer {
    private final Map<Product, Integer> warehouse;
    private final List<Order> canceledReservations;
    private final WarehouseReductionProcessor whReductionProcessor;

    public ReservationStoreService(Map<Product, Integer> warehouse,
                                   List<Order> canceledReservations,
                                   WarehouseReductionProcessor whReductionProcessor) {
        this.warehouse = warehouse;
        this.canceledReservations = canceledReservations;
        this.whReductionProcessor = whReductionProcessor;
    }

    public void cancelReservation(Order order) {
        order.getItems().forEach((product, quantity) ->
                warehouse.computeIfPresent(product, (_, stock) -> stock + quantity));
        System.out.printf("Reservation: Cancelled. %s%n", order);
        canceledReservations.add(order);
    }

    public void takeReservation(Order order) throws InterruptedException {
        Thread.sleep(Randomizer.creationOrderTimeout());
        WarehouseReductionProcessor.ReductionResult processingResult = null;
        try {
            processingResult = whReductionProcessor.reductionProcessing(order);
            if (processingResult.isFailed()) {
                processingResult.rollbackDeductions();
                System.out.printf("Reservation: FAIL. %s out of stock. %s%n",
                        processingResult.getOutOfStock(), order);
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
}
