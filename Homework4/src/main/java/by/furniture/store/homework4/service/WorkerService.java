package by.furniture.store.homework4.service;

import by.furniture.store.homework4.model.Order;
import by.furniture.store.homework4.model.Worker;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingQueue;

@Service
public class WorkerService {
    private final OrderConsumer orderConsumer;
    private final BlockingQueue<Order> orderQueue;

    public WorkerService(OrderConsumer orderConsumer, BlockingQueue<Order> orderQueue) {
        this.orderConsumer = orderConsumer;
        this.orderQueue = orderQueue;
    }

    @Async("storeTaskExecutor")
    public void processOrder(Worker worker) {
        try {
            while (true) {
                Order order = orderQueue.take();
                orderConsumer.processOrder(order, worker.workerName());
            }
        } catch (InterruptedException _) {
            Thread.currentThread().interrupt();
        }
    }
}
