package by.furniture.store.homework4.aspect;

import by.furniture.store.homework4.model.Order;
import by.furniture.store.homework4.model.Product;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Aspect
@Component
public class AnalyticsAspect {

    private final List<Order> processedOrders;

    public AnalyticsAspect(List<Order> processedOrders) {
        this.processedOrders = processedOrders;
    }

    @After("execution(void by.furniture.store.homework4.TestRunner.run(..))")
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
