package by.furniture.store.homework4.model;

import org.springframework.lang.NonNull;

import java.math.BigDecimal;

public record Product(String name, BigDecimal price) {
    @Override
    @NonNull
    public String toString() {
        return "%s ($%s)".formatted(name, price);
    }
}
