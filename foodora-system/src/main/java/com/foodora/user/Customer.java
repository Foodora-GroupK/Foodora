package com.foodora.user;

import com.foodora.model.Order;
import com.foodora.fidelity.FidelityCard;
import com.foodora.fidelity.BasicFidelityCard;
import com.foodora.util.IDGenerator;
import com.foodora.util.Coordinate;
import java.util.*;
import java.util.logging.Logger;

/**
 * Represents a customer in the Foodora system.
 * Customers can place orders, manage their fidelity cards, and receive notifications.
 */
public class Customer extends User {
    private static final Logger LOGGER = Logger.getLogger(Customer.class.getName());
    
    private final String surname;
    private Coordinate address;
    private final String email;
    private String phoneNumber;

    private final List<Order> orderHistory;
    private FidelityCard fidelityCard;
    private int points;
    private boolean notification;

    public Customer(String name, String surname, Coordinate address, String email, 
                   String phoneNumber, String username, String password) {
        super(IDGenerator.generateID("C"), name, username, password);
        
        this.surname = Objects.requireNonNull(surname, "Surname cannot be null");
        this.address = Objects.requireNonNull(address, "Address cannot be null");
        this.email = Objects.requireNonNull(email, "Email cannot be null");
        this.phoneNumber = Objects.requireNonNull(phoneNumber, "Phone number cannot be null");

        this.orderHistory = new ArrayList<>();
        this.fidelityCard = new BasicFidelityCard();
        this.points = 0;
        this.notification = false;
    }

    /**
     * Places a new order and applies any applicable fidelity discounts.
     * @throws IllegalArgumentException if order is null or invalid
     */
    public void placeOrder(Order order) {
        Objects.requireNonNull(order, "Order cannot be null");
        if (order.getCustomer() != this) {
            throw new IllegalArgumentException("Order customer does not match");
        }

        double totalPrice = order.calculateTotalPrice();
        double finalPrice = fidelityCard.applyDiscount(totalPrice, this);
        order.setFinalPrice(finalPrice);
        orderHistory.add(order);
        
        LOGGER.info(String.format("Order placed by %s. Total: %.2f€, Final after discount: %.2f€", 
            getName(), totalPrice, finalPrice));
    }

    /**
     * Registers a new fidelity card for the customer.
     * @throws IllegalArgumentException if card is null
     */
    public void registerFidelityCard(FidelityCard card) {
        this.fidelityCard = Objects.requireNonNull(card, "Fidelity card cannot be null");
    }

    /**
     * Unregisters from current fidelity program and reverts to basic card.
     */
    public void unregisterFidelityCard() {
        this.fidelityCard = new BasicFidelityCard();
        this.points = 0;  // Reset points when unregistering
    }

    public void enableNotification() {
        this.notification = true;
        LOGGER.info("Notifications enabled for customer " + getName());
    }

    public void disableNotification() {
        this.notification = false;
        LOGGER.info("Notifications disabled for customer " + getName());
    }

    /**
     * Sends a notification about a special offer if notifications are enabled.
     */
    public void notifySpecialOffer(Restaurant restaurant, String message) {
        Objects.requireNonNull(restaurant, "Restaurant cannot be null");
        Objects.requireNonNull(message, "Message cannot be null");
        
        if (notification) {
            LOGGER.info(String.format("🔔 Special offer for %s from %s: %s", 
                getName(), restaurant.getName(), message));
        }
    }

    /**
     * Adds points to the customer's fidelity account.
     * @throws IllegalArgumentException if points is negative
     */
    public void addPoints(int pts) {
        if (pts < 0) {
            throw new IllegalArgumentException("Cannot add negative points");
        }
        this.points += pts;
    }

    /**
     * Deducts points from the customer's fidelity account.
     * @throws IllegalArgumentException if points is negative or insufficient balance
     */
    public void deductPoints(int pts) {
        if (pts < 0) {
            throw new IllegalArgumentException("Cannot deduct negative points");
        }
        if (this.points < pts) {
            throw new IllegalArgumentException("Insufficient points balance");
        }
        this.points -= pts;
    }

    public boolean hasNotificationsEnabled() {
        return notification;
    }

    // Getters
    public List<Order> getOrderHistory() {
        return Collections.unmodifiableList(orderHistory);
    }

    public FidelityCard getFidelityCard() {
        return fidelityCard;
    }

    public int getPoints() {
        return points;
    }

    public Coordinate getAddress() {
        return address;
    }

    public void updateAddress(Coordinate newAddress) {
        this.address = Objects.requireNonNull(newAddress, "Address cannot be null");
    }

    public void updatePhoneNumber(String newPhoneNumber) {
        this.phoneNumber = Objects.requireNonNull(newPhoneNumber, "Phone number cannot be null");
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
} 