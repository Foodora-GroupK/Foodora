package com.foodora.policy.order;

import com.foodora.model.Order;
import com.foodora.model.MenuItem;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Policy that analyzes a la carte items and ranks them by least ordered (ascending order).
 */
public class LeastOrderedItemAlaCartePolicy implements OrderSortingPolicy {
    @Override
    public Map<String, Integer> analyzeOrders(List<Order> completedOrders) {
        if (completedOrders == null) {
            throw new IllegalArgumentException("Completed orders list cannot be null");
        }

        Map<String, Integer> countMap = new HashMap<>();

        // Count occurrences of each a la carte item
        for (Order order : completedOrders) {
            if (order.getItems() == null) continue;
            
            for (MenuItem item : order.getItems()) {
                if (item != null) {
                    String name = item.getName();
                    countMap.put(name, countMap.getOrDefault(name, 0) + 1);
                }
            }
        }

        // Sort by count (ascending) and maintain insertion order
        return countMap.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue())
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (e1, e2) -> e1,
                LinkedHashMap::new
            ));
    }
}
