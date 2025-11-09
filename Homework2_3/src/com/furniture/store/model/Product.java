package com.furniture.store.model;

import java.math.BigDecimal;

public record Product(String name, BigDecimal price) {

    @Override
    public String toString() {
        return "%s ($%s)".formatted(name, price);
    }
}
