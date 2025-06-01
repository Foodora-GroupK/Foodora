package com.foodora.policy.order;

import com.foodora.model.Order;
import com.foodora.model.Meal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Policy that analyzes half-meal orders and ranks them by popularity (most ordered first).
 * This can be useful for identifying popular meal combinations and planning inventory.
 */
public class MostOrderedHalfMealPolicy implements OrderSortingPolicy {
    
    @Override
    public Map<String, Integer> analyzeOrders(List<Order> completedOrders) {
        if (completedOrders == null) {
            throw new IllegalArgumentException("Completed orders list cannot be null");
        }

        // Count occurrences of each half-meal
        Map<String, Integer> countMap = new HashMap<>();
        for (Order order : completedOrders) {
            if (order.getMeals() == null) continue;
            
            for (Meal meal : order.getMeals()) {
                if (meal != null && meal.isHalfMeal()) {
                    String name = meal.getName();
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