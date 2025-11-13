package by.furniture.store.homework4.service;

import by.furniture.store.homework4.model.Customer;
import by.furniture.store.homework4.model.Order;
import by.furniture.store.homework4.model.Product;
import by.furniture.store.homework4.model.ReservedOrder;
import by.furniture.store.homework4.util.Randomizer;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomerService {
    private final OrderProducer orderProducer;
    private final ReservationOrderProducer reservationOrderProducer;
    private final List<Product> catalog;

    public CustomerService(OrderProducer orderProducer, ReservationOrderProducer reservationOrderProducer, List<Product> catalog) {
        this.orderProducer = orderProducer;
        this.reservationOrderProducer = reservationOrderProducer;
        this.catalog = catalog;
    }

    @Async("storeTaskExecutor")
    public void createOrder(Customer customer) {
        List<Product> shuffledCatalog = new ArrayList<>(catalog);
        Collections.shuffle(shuffledCatalog);

        Map<Product, Integer> items = new HashMap<>();
        for (int i = 0; i < Randomizer.getOrderItemsCount(); i++) {
            Product p = shuffledCatalog.get(i);
            int quantity = Randomizer.getOrderItemQuantity();
            items.put(p, quantity);
        }

        if (Randomizer.isReservation()) {
            handleReservation(customer, items);
            return;
        }

        orderProducer.submitOrder(new Order(customer.customerName(), items));
    }

    private void handleReservation(Customer customer, Map<Product, Integer> items) {
        try {
            ReservedOrder order = new ReservedOrder(customer.customerName(), items);
            reservationOrderProducer.takeReservation(order);

            Thread.sleep(Randomizer.customerDecisionTimeout());

            if (Randomizer.isDecisionToCancel()) {
                reservationOrderProducer.cancelReservation(order);
            } else {
                orderProducer.submitOrder(order);
            }
        } catch (InterruptedException _) {
            Thread.currentThread().interrupt();
        }
    }
}