package by.furniture.store.homework4.service;

import by.furniture.store.homework4.model.Order;

public interface ReservationOrderProducer {
    void takeReservation(Order order) throws InterruptedException;
    void cancelReservation(Order order);
}
