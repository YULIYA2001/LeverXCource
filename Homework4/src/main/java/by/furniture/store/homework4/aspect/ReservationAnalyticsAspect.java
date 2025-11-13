package by.furniture.store.homework4.aspect;

import by.furniture.store.homework4.model.Order;
import by.furniture.store.homework4.model.Product;
import by.furniture.store.homework4.model.ReservedOrder;
import by.furniture.store.homework4.util.Printable;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Aspect
@org.springframework.core.annotation.Order(1)
@Component
public class ReservationAnalyticsAspect {
    private final List<Order> processedOrders;
    private final List<Order> canceledReservations;
    private final Map<Product, Integer> warehouse;

    public ReservationAnalyticsAspect(List<Order> processedOrders, List<Order> canceledReservations, Map<Product, Integer> warehouse) {
        this.processedOrders = processedOrders;
        this.canceledReservations = canceledReservations;
        this.warehouse = warehouse;
    }

    @After("execution(void by.furniture.store.homework4.TestRunner.run(..))")
    public void runAnalytics() {
        System.out.println("\nReservation analytics\n----------------------------------");

        System.out.println("The total number of canceled reservations: " + canceledReservations.size());

        Map<Product, Integer> maxReservedQuantityAmongReservations = Stream.concat(
                        processedOrders.stream().filter(ReservedOrder.class::isInstance),
                        canceledReservations.stream()
                )
                .map(Order::getItems)
                .flatMap(item -> item.entrySet().stream())
                .parallel()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Integer::max));

        Map<Product, Integer> warehouseInitialState = Stream.concat(
                        processedOrders.stream().map(Order::getItems),
                        Stream.of(warehouse)
                )
                .flatMap(item -> item.entrySet().stream())
                .parallel()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Integer::sum));

        Map<Product, String> maxProductPercentageQuantityInReservedOrder =
                maxReservedQuantityAmongReservations.entrySet().stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                e -> (int) Math.round(
                                        e.getValue() / (double) warehouseInitialState.get(e.getKey()) * 100
                                ) + "%"
                        ));

        System.out.println("Max product quantity (%) in single reserved order: \n"
                + Printable.getPrintableMap(maxProductPercentageQuantityInReservedOrder));
    }
}
