package by.furniture.store.homework4.config;

import by.furniture.store.homework4.model.Order;
import by.furniture.store.homework4.model.Product;
import by.furniture.store.homework4.util.Randomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;

@Configuration
public class AppConfig {
    @Bean
    public Map<Product, Integer> warehouse() {
        return new ConcurrentHashMap<>(Randomizer.getProductsWithQuantity());
    }

    @Bean
    public BlockingQueue<Order> orderQueue() {
        return new LinkedBlockingDeque<>(Randomizer.ORDERS_QUEUE_LENGTH);
    }

    @Bean
    public List<Product> catalog(Map<Product, Integer> warehouse) {
        return new ArrayList<>(warehouse.keySet());
    }

    @Bean
    public List<Order> processedOrders() {
        return Collections.synchronizedList(new ArrayList<>());
    }
}
