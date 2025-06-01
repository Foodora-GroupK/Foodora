package com.foodora;

import com.foodora.user.*;
import com.foodora.model.*;
import com.foodora.util.Coordinate;
import com.foodora.policy.delivery.FastestDeliveryPolicy;
import com.foodora.policy.target.TargetProfitByServiceFee;
import com.foodora.policy.order.MostOrderedHalfMealPolicy;
import java.util.logging.Logger;
import java.util.List;
import java.util.Arrays;

/**
 * Foodora Application Entry Point
 * This class demonstrates the core functionality of the Foodora food delivery system.
 */
public class App {
    private static final Logger LOGGER = Logger.getLogger(App.class.getName());

    public static void main(String[] args) {
        LOGGER.info("Initializing Foodora System...");

        // Get the singleton instance
        MyFoodoraSystem system = MyFoodoraSystem.getInstance();

        // Configure system policies
        system.setDeliveryPolicy(new FastestDeliveryPolicy());
        system.setTargetProfitPolicy(new TargetProfitByServiceFee());
        system.setOrderSortingPolicy(new MostOrderedHalfMealPolicy());

        // Set initial business parameters
        system.setFees(5.0, 0.1, 10.0); // serviceFee, markupPercentage, deliveryCost

        // Create and register a restaurant
        Restaurant pizzaPlace = new Restaurant(
            "Pizza Palace",
            new Coordinate(40.7128, -74.0060),
            "pizza_palace",
            "secure123"
        );

        // Add menu items
        MenuItem margherita = new MenuItem("Margherita Pizza", 12.99, "Classic tomato and mozzarella");
        MenuItem pepperoni = new MenuItem("Pepperoni Pizza", 14.99, "Spicy pepperoni with cheese");
        pizzaPlace.addMenuItem(margherita);
        pizzaPlace.addMenuItem(pepperoni);

        // Create a meal
        pizzaPlace.createMeal(
            "Pizza Combo",
            Arrays.asList(margherita, pepperoni),
            Meal.MealType.MAIN_DISH,
            Meal.MealSize.STANDARD,
            true  // meal of the week
        );

        // Register users
        Customer customer = new Customer(
            "John Doe",
            new Coordinate(40.7130, -74.0065),
            "john_doe",
            "password123"
        );

        Courier courier = new Courier(
            "Mike",
            "Smith",
            new Coordinate(40.7125, -74.0063),
            "555-0123",
            "mike_courier",
            "pass456"
        );

        // Add users to system
        system.addUser(pizzaPlace);
        system.addUser(customer);
        system.addUser(courier);

        LOGGER.info("Foodora System initialized successfully!");
        LOGGER.info("Registered users: " + system.getUsers().size());
        
        // Example of system capabilities
        courier.setOnDuty(true);
        customer.enableNotifications(true);
        
        LOGGER.info("System is ready to process orders!");
    }
}
