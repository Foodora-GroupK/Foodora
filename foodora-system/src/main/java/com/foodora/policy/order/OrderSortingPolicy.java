package com.foodora.policy.order;

import com.foodora.model.Order;
import java.util.List;
import java.util.Map;

/**
 * Interface for analyzing and sorting orders based on different criteria.
 * Implementations can provide different strategies for analyzing order patterns.
 */
public interface OrderSortingPolicy {
    /**
     * Analyzes the completed orders and returns statistics based on the policy's criteria.
     * @param completedOrders List of completed orders to analyze
     * @return Map of item names to their order counts, sorted according to the policy
     * @throws IllegalArgumentException if completedOrders is null
     */
    Map<String, Integer> analyzeOrders(List<Order> completedOrders);
} 