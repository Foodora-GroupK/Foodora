package com.foodora.policy.order;

import com.foodora.model.Order;
import com.foodora.model.MenuItem;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Policy that analyzes individual menu items ordered a la carte and ranks them by popularity.
 * This can be useful for identifying popular individual dishes and planning inventory.
 */
public class MostOrderedItemAlaCartePolicy implements OrderSortingPolicy {
    
    @Override
    public Map<String, Integer> analyzeOrders(List<Order> completedOrders) {
        if (completedOrders == null) {
            throw new IllegalArgumentException("Completed orders list cannot be null");
        }

        // Count occurrences of each menu item
        Map<String, Integer> countMap = new HashMap<>();
        for (Order order : completedOrders) {
            if (order.getItems() == null) continue;
            
            for (MenuItem item : order.getItems()) {
                if (item != null) {
                    String name = item.getName();
                    countMap.put(name, countMap.getOrDefault(name, 0) + 1);
                }
            }
        }

        // Sort by count (descending) and maintain insertion order
        return countMap.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (e1, e2) -> e1,
                LinkedHashMap::new
            ));
    }
} 