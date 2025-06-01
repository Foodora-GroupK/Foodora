package com.foodora.user;

import com.foodora.model.Menu;
import com.foodora.model.Meal;
import com.foodora.model.MenuItem;
import com.foodora.util.Coordinate;
import com.foodora.util.IDGenerator;
import com.foodora.MyFoodoraSystem;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Represents a restaurant in the Foodora system.
 * Restaurants can manage their menu, create meals, and set discount policies.
 */
public class Restaurant extends User {
    private static final Logger LOGGER = Logger.getLogger(Restaurant.class.getName());
    
    private static final double MIN_DISCOUNT = 0.0;
    private static final double MAX_DISCOUNT = 0.5; // Maximum 50% discount
    
    private final Coordinate location;
    private final Menu menu;
    private final List<Meal> meals;

    private double defaultGenericDiscountFactor = 0.05; // 5% default for regular meals
    private double defaultSpecialDiscountFactor = 0.10; // 10% default for meal-of-the-week
    
    public Restaurant(String name, Coordinate location, String username, String password) {
        super(IDGenerator.generateID("R"), name, username, password);
        this.location = Objects.requireNonNull(location, "Location cannot be null");
        this.menu = new Menu();
        this.meals = new ArrayList<>();
    }

    // Menu Management
    public void addMenuItem(MenuItem item) {
        Objects.requireNonNull(item, "Menu item cannot be null");
        menu.addItem(item);
        LOGGER.info(String.format("Added menu item: %s to restaurant %s", item.getName(), getName()));
        
        // Notify customers of new menu item
        notifyCustomers(String.format("New item added to menu: %s", item.getName()));
    }

    public void removeMenuItem(MenuItem item) {
        Objects.requireNonNull(item, "Menu item cannot be null");
        menu.removeItem(item);
        LOGGER.info(String.format("Removed menu item: %s from restaurant %s", item.getName(), getName()));
    }

    public Menu getMenu() {
        return menu;
    }

    // Meal Management
    public void createMeal(String name, List<MenuItem> items, Meal.MealType type, 
                         Meal.MealSize size, boolean isMealOfTheWeek) {
        Objects.requireNonNull(name, "Meal name cannot be null");
        Objects.requireNonNull(items, "Items list cannot be null");
        Objects.requireNonNull(type, "Meal type cannot be null");
        Objects.requireNonNull(size, "Meal size cannot be null");
        
        if (items.isEmpty()) {
            throw new IllegalArgumentException("Meal must contain at least one item");
        }

        // Verify all items are from this restaurant's menu
        if (!menu.containsAll(items)) {
            throw new IllegalArgumentException("All items must be from this restaurant's menu");
        }

        double discount = isMealOfTheWeek ? defaultSpecialDiscountFactor : defaultGenericDiscountFactor;
        Meal newMeal = new Meal(name, items, type, size, isMealOfTheWeek, discount);
        meals.add(newMeal);
        
        LOGGER.info(String.format("Created new meal: %s at restaurant %s", name, getName()));
        
        if (isMealOfTheWeek) {
            notifyCustomers(String.format("New meal of the week: %s!", name));
        }
    }

    public void removeMeal(Meal meal) {
        Objects.requireNonNull(meal, "Meal cannot be null");
        meals.remove(meal);
        LOGGER.info(String.format("Removed meal: %s from restaurant %s", meal.getName(), getName()));
    }

    public List<Meal> getMeals() {
        return Collections.unmodifiableList(meals);
    }

    // Discount Management
    public void setDefaultGenericDiscountFactor(double factor) {
        validateDiscountFactor(factor, "Generic discount");
        this.defaultGenericDiscountFactor = factor;
        LOGGER.info(String.format("Updated generic discount to %.1f%% for restaurant %s", 
            factor * 100, getName()));
    }

    public void setDefaultSpecialDiscountFactor(double factor) {
        validateDiscountFactor(factor, "Special discount");
        this.defaultSpecialDiscountFactor = factor;
        LOGGER.info(String.format("Updated special discount to %.1f%% for restaurant %s", 
            factor * 100, getName()));
    }

    private void validateDiscountFactor(double factor, String discountType) {
        if (factor < MIN_DISCOUNT || factor > MAX_DISCOUNT) {
            throw new IllegalArgumentException(
                String.format("%s factor must be between %.0f%% and %.0f%%", 
                    discountType, MIN_DISCOUNT * 100, MAX_DISCOUNT * 100));
        }
    }

    // Notification System
    private void notifyCustomers(String message) {
        List<Customer> customersToNotify = MyFoodoraSystem.getInstance().getUsers().stream()
            .filter(user -> user instanceof Customer)
            .map(user -> (Customer) user)
            .filter(Customer::hasNotificationsEnabled)
            .collect(Collectors.toList());

        for (Customer customer : customersToNotify) {
            customer.notifySpecialOffer(this, message);
        }

        if (!customersToNotify.isEmpty()) {
            LOGGER.info(String.format("Notified %d customers about: %s", 
                customersToNotify.size(), message));
        }
    }

    // Getters
    public double getDefaultGenericDiscountFactor() {
        return defaultGenericDiscountFactor;
    }

    public double getDefaultSpecialDiscountFactor() {
        return defaultSpecialDiscountFactor;
    }

    public Coordinate getLocation() {
        return location;
    }

    // authenticate
    @Override
    public boolean authenticate(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }
} 