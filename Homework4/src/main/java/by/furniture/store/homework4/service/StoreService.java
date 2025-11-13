package by.furniture.store.homework4.service;

import by.furniture.store.homework4.model.Order;
import by.furniture.store.homework4.model.ReservedOrder;
import by.furniture.store.homework4.util.Randomizer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.BlockingQueue;

@Service
public class StoreService implements OrderConsumer, OrderProducer {
    private final BlockingQueue<Order> orderQueue;
    private final List<Order> processedOrders;
    private final WarehouseReductionProcessor whReductionProcessor;

    public StoreService(BlockingQueue<Order> orderQueue,
                        List<Order> processedOrders,
                        WarehouseReductionProcessor whReductionProcessor) {
        this.orderQueue = orderQueue;
        this.processedOrders = processedOrders;
        this.whReductionProcessor = whReductionProcessor;
    }

    public void processOrder(Order order, String workerName) throws InterruptedException {
        Thread.sleep(Randomizer.processOrderTimeout());

        if (order instanceof ReservedOrder) {
            processedSuccessfully(order, workerName);
            return;
        }

        WarehouseReductionProcessor.ReductionResult rpResult = null;
        try {
            rpResult = whReductionProcessor.reductionProcessing(order);

            if (rpResult.isFailed()) {
                rpResult.rollbackDeductions();
                System.out.printf("Process (%s): FAIL - #%d. %s out of stock%n",
                        workerName, order.getOrderId(), rpResult.getOutOfStock());
            } else {
                processedSuccessfully(order, workerName);
            }
        } catch (Exception e) {
            if (rpResult != null) {
                rpResult.rollbackDeductions();
            }
            throw e;
        }
    }

    private void processedSuccessfully(Order order, String workerName) {
        processedOrders.add(order);
        System.out.printf("Process (%s): SUCCESS - #%d%n", workerName, order.getOrderId());
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
}
