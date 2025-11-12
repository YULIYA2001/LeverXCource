package by.furniture.store.homework4;

import by.furniture.store.homework4.model.Customer;
import by.furniture.store.homework4.model.Product;
import by.furniture.store.homework4.model.Worker;
import by.furniture.store.homework4.service.CustomerService;
import by.furniture.store.homework4.service.WorkerService;
import by.furniture.store.homework4.util.Randomizer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class TestRunner implements CommandLineRunner {

    private static final String STAR_SEPARATOR = "*******************************";
    private static final String DASH_SEPARATOR = "-------------------------------";

    private final CustomerService customerService;
    private final WorkerService workerService;
    private final Map<Product, Integer> warehouse;
    private final ThreadPoolTaskExecutor executor;

    public TestRunner(CustomerService customerService,
                      WorkerService workerService,
                      Map<Product, Integer> warehouse,
                      @Qualifier("storeTaskExecutor") ThreadPoolTaskExecutor executor) {
        this.customerService = customerService;
        this.workerService = workerService;
        this.warehouse = warehouse;
        this.executor = executor;
    }

    @Override
    public void run(String... args) throws Exception {
        printStoreStartInfo();
        simulateWork();
        printStoreFinishInfo();
    }

    private void simulateWork() throws InterruptedException {
        for (int i = 0; i < Randomizer.WORKERS_COUNT; i++) {
            workerService.processOrder(new Worker("Worker-" + (i + 1)));
        }

        for (int i = 0; i < Randomizer.CUSTOMERS_COUNT; i++) {
            customerService.createOrder(new Customer("Customer-" + (i + 1)));
        }

        TimeUnit.SECONDS.sleep(Randomizer.FINISH_WAITING_TIME);
        executor.shutdown();
        if (!executor.getThreadPoolExecutor().awaitTermination(Randomizer.FINISH_WAITING_TIME, TimeUnit.SECONDS)) {
            executor.getThreadPoolExecutor().shutdownNow();
        }
    }

    private void printStoreStartInfo() {
        System.out.println(STAR_SEPARATOR + "\nFurniture store started\n" + STAR_SEPARATOR);
        System.out.println("Customers count: " + Randomizer.CUSTOMERS_COUNT);
        System.out.println("Workers count: " + Randomizer.WORKERS_COUNT);
        System.out.println("Order queue length: " + Randomizer.ORDERS_QUEUE_LENGTH);
        System.out.println(DASH_SEPARATOR);
        System.out.println("Warehouse: \n" + getPrintableWarehouse());
    }

    private void printStoreFinishInfo() {
        System.out.println("\n" + STAR_SEPARATOR + "\nFurniture store finished\n" + STAR_SEPARATOR);
        System.out.println("Warehouse: \n" + getPrintableWarehouse());
    }

    private String getPrintableWarehouse() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Product, Integer> entry : warehouse.entrySet()) {
            sb.append("- ").append(entry.getKey()).append(" - ").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }
}
