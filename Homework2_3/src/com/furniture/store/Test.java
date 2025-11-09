package com.furniture.store;

import com.furniture.store.core.Store;
import com.furniture.store.model.Customer;
import com.furniture.store.model.Product;
import com.furniture.store.model.Worker;
import com.furniture.store.service.CustomerOrderExecutor;
import com.furniture.store.service.WorkerExecutor;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Test {
    public static final String STAR_SEPARATOR = "*******************************";
    public static final String DASH_SEPARATOR = "-------------------------------";

    public static final int CUSTOMERS_COUNT = 15;
    public static final int WORKERS_COUNT = 2;
    private static final int ORDERS_QUEUE_LENGTH = 3;
    private static final long FINISH_WAITING_TIME = 3;

    public static void main(String[] args) {
        Store store = new Store(ORDERS_QUEUE_LENGTH);

        printStoreStartInfo(store);
        simulateWork(store);
        printStoreFinishInfo(store);

        store.new Analytics().runAnalytics();
    }

    private static void simulateWork(Store store) {
        List<Product> catalog = store.getWarehouse().keySet().stream().toList();
        ExecutorService executor = Executors.newFixedThreadPool(WORKERS_COUNT + CUSTOMERS_COUNT);

        for (int i = 0; i < WORKERS_COUNT; i++) {
            executor.execute(new WorkerExecutor(
                    new Worker("Worker-" + (i + 1)),
                    store,
                    store.getOrderQueue()
            ));
        }

        for (int i = 0; i < CUSTOMERS_COUNT; i++) {
            executor.execute(new CustomerOrderExecutor(
                    new Customer("Customer-" + (i + 1)),
                    store,
                    catalog
            ));
        }

        try {
            TimeUnit.SECONDS.sleep(FINISH_WAITING_TIME);
            executor.shutdown();

            if (!executor.awaitTermination(FINISH_WAITING_TIME, TimeUnit.SECONDS)) {
                executor.shutdownNow();
                if (!executor.awaitTermination(FINISH_WAITING_TIME, TimeUnit.SECONDS))
                    System.err.println("Executor did not terminate");
            }
        } catch (InterruptedException _) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private static void printStoreStartInfo(Store store) {
        System.out.println(STAR_SEPARATOR + "\nFurniture store started\n" + STAR_SEPARATOR);
        System.out.println("Customers count: " + CUSTOMERS_COUNT);
        System.out.println("Workers count: " + WORKERS_COUNT);
        System.out.println("Order queue length: " + ORDERS_QUEUE_LENGTH);
        System.out.println(DASH_SEPARATOR);
        System.out.println("Warehouse: \n" + convertToPrintable(store.getWarehouse()));
    }

    private static void printStoreFinishInfo(Store store) {
        System.out.println("\n" + STAR_SEPARATOR + "\nFurniture store finished\n" + STAR_SEPARATOR);
        System.out.println("Warehouse: \n" + convertToPrintable(store.getWarehouse()));
    }

    private static String convertToPrintable(Map<Product, Integer> warehouse) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Product, Integer> entry : warehouse.entrySet()) {
            sb.append("- ").append(entry.getKey()).append(" - ").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }
}