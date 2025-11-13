package by.furniture.store.homework4.util;

import by.furniture.store.homework4.model.Product;

import java.util.Map;

public class Printable {
    public static <T> String getPrintableMap(Map<Product, T> map) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Product, T> entry : map.entrySet()) {
            sb.append("- ").append(entry.getKey()).append(" - ").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }

    private Printable() {}
}
