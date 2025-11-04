package com.furniture.store;

import java.util.concurrent.BlockingQueue;

class Worker implements Runnable {
    private final String workerName;
    private final OrderConsumer orderConsumer;
    private final BlockingQueue<Order> queue;

    public Worker(String workerName, OrderConsumer orderConsumer, BlockingQueue<Order> queue) {
        this.workerName = workerName;
        this.orderConsumer = orderConsumer;
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Order order = queue.take();
                orderConsumer.processOrder(order, workerName);
            }
        } catch (InterruptedException _) {
            Thread.currentThread().interrupt();
        }
    }
}
