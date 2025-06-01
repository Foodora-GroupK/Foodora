package com.foodora.model;

import com.foodora.user.Customer;
import com.foodora.user.Courier;
import com.foodora.user.Restaurant;
import com.foodora.util.IDGenerator;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

public class Order {
    public enum OrderStatus {
        CREATED, PREPARING, READY_FOR_DELIVERY, IN_DELIVERY, DELIVERED, CANCELLED
    }

    private final String orderId;
    private final LocalDateTime createdAt;
    private OrderStatus status;
    private Customer customer;
    private Restaurant restaurant;
    private Courier courier;

    private List<MenuItem> items;
    private List<Meal> meals;

    private double finalPrice;

    public Order(Customer customer, Restaurant restaurant, Courier courier, List<MenuItem> items, List<Meal> meals) {
        if (customer == null || restaurant == null) {
            throw new IllegalArgumentException("Customer and restaurant cannot be null");
        }
        
        this.orderId = IDGenerator.generateID("O");
        this.createdAt = LocalDateTime.now();
        this.status = OrderStatus.CREATED;
        
        this.customer = customer;
        this.restaurant = restaurant;
        this.courier = courier;
        this.items = items != null ? items : new ArrayList<>();
        this.meals = meals != null ? meals : new ArrayList<>();
    }

    public double calculateTotalPrice() {
        double itemTotal = items.stream().mapToDouble(MenuItem::getPrice).sum();
        double mealTotal = meals.stream().mapToDouble(Meal::getPrice).sum();
        return itemTotal + mealTotal;
    }

    // Getters
    public String getOrderId() { return orderId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public OrderStatus getStatus() { return status; }
    public Customer getCustomer() { return customer; }
    public Restaurant getRestaurant() { return restaurant; }
    public Courier getCourier() { return courier; }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void setFinalPrice(double finalPrice) {
        this.finalPrice = finalPrice;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public List<MenuItem> getItems() { return items; }
    public List<Meal> getMeals() { return meals; }

    public void setCourier(Courier courier) {
        this.courier = courier;
    }
} 