package com.furniture.store;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Randomizer {
    private static final List<String> PRODUCT_NAMES = List.of(
            "Table", "Chair", "Bed", "Bookcase", "Carpet",
            "Dresser", "Sofa", "Wardrobe", "Desk", "Shelf"
    );

    private static final int MONEY_SCALE = 2;
    private static final int MIN_PRODUCT_QUANTITY = 10;
    private static final int MAX_PRODUCT_QUANTITY = 40;
    private static final int MIN_PRODUCT_PRICE = 100;
    private static final int MAX_PRODUCT_PRICE = 1000;
    private static final int MAX_ITEMS_IN_ORDER = 3;
    private static final int MAX_PRODUCT_QUANTITY_IN_ORDER = 6;

    public static Map<Product, Integer> getProductsWithQuantity() {
        return PRODUCT_NAMES.stream()
                .map(name -> new Product(name, getProductPrice()))
                .collect(Collectors.toMap(
                        product -> product,
                        _ -> getRandomInt(MIN_PRODUCT_QUANTITY, MAX_PRODUCT_QUANTITY)
                ));
    }

    public static int getOrderItemsCount() {
        return getRandomInt(1, MAX_ITEMS_IN_ORDER);
    }

    public static int getOrderItemQuantity() {
        return getRandomInt(1, MAX_PRODUCT_QUANTITY_IN_ORDER);
    }

    public static long processOrderTimeout() {
        return getRandomInt(100, 500);
    }

    public static long creationOrderTimeout() {
        return getRandomInt(500, 1000);
    }

    private static BigDecimal getProductPrice() {
        double value = ThreadLocalRandom.current().nextDouble(MIN_PRODUCT_PRICE, MAX_PRODUCT_PRICE);
        return BigDecimal.valueOf(value).setScale(MONEY_SCALE, RoundingMode.HALF_UP);
    }

    private static int getRandomInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    private Randomizer() {}
}
