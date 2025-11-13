package by.furniture.store.homework4.service;

import by.furniture.store.homework4.model.Order;
import by.furniture.store.homework4.model.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WarehouseReductionProcessor {
    private final Map<Product, Integer> warehouse;

    public WarehouseReductionProcessor(Map<Product, Integer> warehouse) {
        this.warehouse = warehouse;
    }

    public ReductionResult reductionProcessing(Order order) {
        Map<Product, Integer> deductions = new HashMap<>();
        List<Product> outOfStock = new ArrayList<>();

        boolean success = order.getItems().entrySet().stream()
                .allMatch(item -> warehouse.computeIfPresent(
                        item.getKey(), (product, currentStock) -> {
                            if (currentStock >= item.getValue()) {
                                deductions.put(product, item.getValue());
                                return currentStock - item.getValue();
                            }
                            outOfStock.add(product);
                            return currentStock;
                        }) != null && deductions.containsKey(item.getKey()));

        return new ReductionResult(success, deductions, outOfStock);
    }

    public class ReductionResult {
        private final boolean isSuccess;
        private final Map<Product, Integer> deductions;
        private final List<Product> outOfStock;

        ReductionResult(boolean isSuccess, Map<Product, Integer> deductions, List<Product> outOfStock) {
            this.isSuccess = isSuccess;
            this.deductions = deductions;
            this.outOfStock = outOfStock;
        }

        public boolean isFailed() {
            return !isSuccess;
        }

        public List<Product> getOutOfStock() {
            return outOfStock;
        }

        void rollbackDeductions() {
            deductions.forEach((product, quantity) ->
                    warehouse.computeIfPresent(product, (_, stock) -> stock + quantity));
        }
    }
}
