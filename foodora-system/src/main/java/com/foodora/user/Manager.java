package com.foodora.user;

import com.foodora.MyFoodoraSystem;
import com.foodora.model.Order;
import com.foodora.policy.delivery.DeliveryPolicy;
import com.foodora.util.IDGenerator;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Manager class representing system administrators in the Foodora system.
 * Managers can configure system settings, manage users, and view business metrics.
 */
public class Manager extends User {
    private static final Logger LOGGER = Logger.getLogger(Manager.class.getName());
    
    private final String surname;

    public Manager(String name, String surname, String username, String password) {
        super(IDGenerator.generateID("M"), name, username, password);
        this.surname = Objects.requireNonNull(surname, "Surname cannot be null");
    }

    // User Management
    public void addUser(User user) {
        Objects.requireNonNull(user, "User cannot be null");
        MyFoodoraSystem.getInstance().addUser(user);
        LOGGER.info(String.format("Added user: %s (%s)", user.getName(), user.getId()));
    }

    public void removeUser(User user) {
        Objects.requireNonNull(user, "User cannot be null");
        MyFoodoraSystem.getInstance().removeUser(user);
        LOGGER.info(String.format("Removed user: %s (%s)", user.getName(), user.getId()));
    }

    public void activateUser(User user) {
        Objects.requireNonNull(user, "User cannot be null");
        user.setActive(true);
        LOGGER.info(String.format("Activated user: %s (%s)", user.getName(), user.getId()));
    }

    public void deactivateUser(User user) {
        Objects.requireNonNull(user, "User cannot be null");
        user.setActive(false);
        LOGGER.info(String.format("Deactivated user: %s (%s)", user.getName(), user.getId()));
    }
    
    // System Configuration
    public void setServiceFeePercentage(double serviceFee) {
        validatePercentage(serviceFee, "Service fee");
        MyFoodoraSystem system = MyFoodoraSystem.getInstance();
        system.setFees(serviceFee, system.getMarkupPercentage(), system.getDeliveryCost());
        LOGGER.info(String.format("Updated service fee to %.2f%%", serviceFee * 100));
    }

    public void setMarkupPercentage(double markup) {
        validatePercentage(markup, "Markup");
        MyFoodoraSystem system = MyFoodoraSystem.getInstance();
        system.setFees(system.getServiceFee(), markup, system.getDeliveryCost());
        LOGGER.info(String.format("Updated markup to %.2f%%", markup * 100));
    }

    public void setDeliveryCost(double deliveryCost) {
        if (deliveryCost < 0) {
            throw new IllegalArgumentException("Delivery cost cannot be negative");
        }
        MyFoodoraSystem system = MyFoodoraSystem.getInstance();
        system.setFees(system.getServiceFee(), system.getMarkupPercentage(), deliveryCost);
        LOGGER.info(String.format("Updated delivery cost to %.2f€", deliveryCost));
    }

    // Business Metrics
    public double computeTotalIncome(List<Order> orders) {
        Objects.requireNonNull(orders, "Orders list cannot be null");
        return MyFoodoraSystem.getInstance().computeTotalIncome();
    }

    public double computeTotalProfit(List<Order> orders) {
        Objects.requireNonNull(orders, "Orders list cannot be null");
        return MyFoodoraSystem.getInstance().computeTotalProfit();
    }

    public double computeAverageIncomePerCustomer(List<Order> orders) {
        Objects.requireNonNull(orders, "Orders list cannot be null");
        return MyFoodoraSystem.getInstance().computeAvgIncomePerCustomer();
    }

    public void optimizeForTargetProfit(double targetProfit) {
        if (targetProfit < 0) {
            throw new IllegalArgumentException("Target profit cannot be negative");
        }
        MyFoodoraSystem.getInstance().optimizeForTargetProfit(targetProfit);
        LOGGER.info(String.format("Optimized system for target profit: %.2f€", targetProfit));
    }

    public void determineDeliveryPolicy(DeliveryPolicy policy) {
        Objects.requireNonNull(policy, "Delivery policy cannot be null");
        MyFoodoraSystem.getInstance().setDeliveryPolicy(policy);
        LOGGER.info("Updated delivery policy to: " + policy.getClass().getSimpleName());
    }

    // Analytics
    public Restaurant getMostSellingRestaurant(List<Order> orders) {
        Objects.requireNonNull(orders, "Orders list cannot be null");
        Map<Restaurant, Integer> salesCount = countRestaurantOrders(orders);

        return salesCount.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(null);
    }

    public Restaurant getLeastSellingRestaurant(List<Order> orders) {
        Objects.requireNonNull(orders, "Orders list cannot be null");
        Map<Restaurant, Integer> salesCount = countRestaurantOrders(orders, true);

        return salesCount.entrySet().stream()
            .min(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(null);
    }

    public Courier getMostActiveCourier(List<Order> orders) {
        Objects.requireNonNull(orders, "Orders list cannot be null");
        Map<Courier, Integer> deliveryCount = countCourierDeliveries(orders);

        return deliveryCount.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(null);
    }

    public Courier getLeastActiveCourier(List<Order> orders) {
        Objects.requireNonNull(orders, "Orders list cannot be null");
        Map<Courier, Integer> deliveryCount = countCourierDeliveries(orders, true);

        return deliveryCount.entrySet().stream()
            .min(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(null);
    }

    // Helper methods
    private void validatePercentage(double value, String fieldName) {
        if (value < 0 || value > 1) {
            throw new IllegalArgumentException(
                fieldName + " must be between 0 and 1 (0% to 100%)");
        }
    }

    private Map<Restaurant, Integer> countRestaurantOrders(List<Order> orders, boolean includeAll) {
        Map<Restaurant, Integer> salesCount = new HashMap<>();
        
        if (includeAll) {
            // Initialize all restaurants with 0 orders
            MyFoodoraSystem.getInstance().getUsers().stream()
                .filter(u -> u instanceof Restaurant)
                .map(u -> (Restaurant) u)
                .forEach(r -> salesCount.put(r, 0));
        }
        
        // Count orders per restaurant
        for (Order order : orders) {
            Restaurant restaurant = order.getRestaurant();
            if (restaurant != null) {
                salesCount.merge(restaurant, 1, Integer::sum);
            }
        }
        
        return salesCount;
    }

    private Map<Restaurant, Integer> countRestaurantOrders(List<Order> orders) {
        return countRestaurantOrders(orders, false);
    }

    private Map<Courier, Integer> countCourierDeliveries(List<Order> orders, boolean includeAll) {
        Map<Courier, Integer> deliveryCount = new HashMap<>();
        
        if (includeAll) {
            // Initialize all couriers with 0 deliveries
            MyFoodoraSystem.getInstance().getUsers().stream()
                .filter(u -> u instanceof Courier)
                .map(u -> (Courier) u)
                .forEach(c -> deliveryCount.put(c, 0));
        }
        
        // Count deliveries per courier
        for (Order order : orders) {
            Courier courier = order.getCourier();
            if (courier != null) {
                deliveryCount.merge(courier, 1, Integer::sum);
            }
        }
        
        return deliveryCount;
    }

    private Map<Courier, Integer> countCourierDeliveries(List<Order> orders) {
        return countCourierDeliveries(orders, false);
    }

    // Getters
    public String getSurname() {
        return surname;
    }
}