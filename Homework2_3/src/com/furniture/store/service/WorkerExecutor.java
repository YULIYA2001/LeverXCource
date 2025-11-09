package com.furniture.store.service;

import com.furniture.store.core.OrderConsumer;
import com.furniture.store.model.Order;
import com.furniture.store.model.Worker;

import java.util.concurrent.BlockingQueue;

public class WorkerExecutor implements Runnable {
    private final Worker worker;
    private final OrderConsumer orderConsumer;
    private final BlockingQueue<Order> queue;

    public WorkerExecutor(Worker worker, OrderConsumer orderConsumer, BlockingQueue<Order> queue) {
        this.worker = worker;
        this.orderConsumer = orderConsumer;
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Order order = queue.take();
                orderConsumer.processOrder(order, worker.workerName());
            }
        } catch (InterruptedException _) {
            Thread.currentThread().interrupt();
        }
    }
}
