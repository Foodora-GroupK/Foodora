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
import java.util.ArrayList;
import java.util.stream.Collectors;
import static com.foodora.model.MenuItem.Category.*;
import static com.foodora.model.MenuItem.Type.STANDARD;
import static com.foodora.model.MenuItem.Type.VEGETARIAN;
import static com.foodora.model.Meal.MealSize.*;
import static com.foodora.model.Meal.MealType;

/**
 * Foodora Application Entry Point
 * Initializes the system with a realistic startup scenario including managers,
 * restaurants, couriers, customers, and meals.
 */
public class App {
    private static final Logger LOGGER = Logger.getLogger(App.class.getName());

    public static void main(String[] args) {
        LOGGER.info("Initializing Foodora System...");
        MyFoodoraSystem system = MyFoodoraSystem.getInstance();

        // Configure system policies
        system.setDeliveryPolicy(new FastestDeliveryPolicy());
        system.setTargetProfitPolicy(new TargetProfitByServiceFee());
        system.setOrderSortingPolicy(new MostOrderedHalfMealPolicy());
        system.setFees(5.0, 0.1, 10.0);

        // 1. Initialize Managers
        initializeManagers(system);

        // 2. Initialize Restaurants with Meals
        initializeRestaurants(system);

        // 3. Initialize Couriers
        initializeCouriers(system);

        // 4. Initialize Customers
        initializeCustomers(system);

        // 5. Send Special Offers Notifications
        sendInitialNotifications(system);

        LOGGER.info("Foodora System initialized successfully!");
        logSystemStatus(system);
    }

    private static void initializeManagers(MyFoodoraSystem system) {
        Manager ceo = new Manager("John", "Smith", "ceo", "ceo123");
        Manager deputy = new Manager("Jane", "Doe", "deputy", "deputy123");
        system.addUser(ceo);
        system.addUser(deputy);
        LOGGER.info("Managers initialized: CEO and Deputy");
    }

    private static void initializeRestaurants(MyFoodoraSystem system) {
        // Restaurant 1: Italian Restaurant
        Restaurant italian = createRestaurant("La Bella Italia", 40.7128, -74.0060);
        createItalianMenu(italian);
        system.addUser(italian);

        // Restaurant 2: Asian Fusion
        Restaurant asian = createRestaurant("Asian Fusion", 40.7580, -73.9855);
        createAsianMenu(asian);
        system.addUser(asian);

        // Restaurant 3: American Diner
        Restaurant american = createRestaurant("Classic Diner", 40.7829, -73.9654);
        createAmericanMenu(american);
        system.addUser(american);

        // Restaurant 4: Mexican Grill
        Restaurant mexican = createRestaurant("El Sabor", 40.7214, -74.0052);
        createMexicanMenu(mexican);
        system.addUser(mexican);

        // Restaurant 5: French Bistro
        Restaurant french = createRestaurant("Le Petit Bistro", 40.7589, -73.9851);
        createFrenchMenu(french);
        system.addUser(french);

        LOGGER.info("Restaurants initialized with menus and meals");
    }

    private static Restaurant createRestaurant(String name, double lat, double lon) {
        return new Restaurant(
            name,
            new Coordinate(lat, lon),
            name.toLowerCase().replace(" ", "_"),
            "Rest" + name.substring(0, 3) + "123!"
        );
    }

    private static void createItalianMenu(Restaurant restaurant) {
        // Menu items
        MenuItem pizza = new MenuItem("Margherita Pizza", 14.99, MAIN_DISH, STANDARD, false);
        MenuItem pasta = new MenuItem("Spaghetti Carbonara", 16.99, MAIN_DISH, STANDARD, false);
        MenuItem salad = new MenuItem("Caprese Salad", 10.99, STARTER, VEGETARIAN, true);
        MenuItem tiramisu = new MenuItem("Tiramisu", 8.99, DESSERT, STANDARD, false);

        restaurant.addMenuItem(pizza);
        restaurant.addMenuItem(pasta);
        restaurant.addMenuItem(salad);
        restaurant.addMenuItem(tiramisu);

        // Create full meals
        createStandardMeals(restaurant, Arrays.asList(pizza, pasta, salad, tiramisu));
    }

    private static void createAsianMenu(Restaurant restaurant) {
        MenuItem sushi = new MenuItem("Sushi Platter", 24.99, MAIN_DISH, STANDARD, true);
        MenuItem ramen = new MenuItem("Tonkotsu Ramen", 16.99, MAIN_DISH, STANDARD, false);
        MenuItem springRolls = new MenuItem("Spring Rolls", 8.99, STARTER, VEGETARIAN, true);
        MenuItem mochi = new MenuItem("Mochi Ice Cream", 6.99, DESSERT, VEGETARIAN, true);

        restaurant.addMenuItem(sushi);
        restaurant.addMenuItem(ramen);
        restaurant.addMenuItem(springRolls);
        restaurant.addMenuItem(mochi);

        createStandardMeals(restaurant, Arrays.asList(sushi, ramen, springRolls, mochi));
    }

    private static void createAmericanMenu(Restaurant restaurant) {
        MenuItem burger = new MenuItem("Classic Burger", 15.99, MAIN_DISH, STANDARD, false);
        MenuItem wings = new MenuItem("Buffalo Wings", 13.99, STARTER, STANDARD, true);
        MenuItem fries = new MenuItem("Loaded Fries", 8.99, STARTER, VEGETARIAN, true);
        MenuItem shake = new MenuItem("Milkshake", 6.99, DESSERT, VEGETARIAN, true);

        restaurant.addMenuItem(burger);
        restaurant.addMenuItem(wings);
        restaurant.addMenuItem(fries);
        restaurant.addMenuItem(shake);

        createStandardMeals(restaurant, Arrays.asList(burger, wings, fries, shake));
    }

    private static void createMexicanMenu(Restaurant restaurant) {
        MenuItem tacos = new MenuItem("Street Tacos", 12.99, MAIN_DISH, STANDARD, true);
        MenuItem burrito = new MenuItem("Grande Burrito", 14.99, MAIN_DISH, STANDARD, false);
        MenuItem nachos = new MenuItem("Loaded Nachos", 10.99, STARTER, VEGETARIAN, true);
        MenuItem flan = new MenuItem("Flan", 7.99, DESSERT, STANDARD, true);

        restaurant.addMenuItem(tacos);
        restaurant.addMenuItem(burrito);
        restaurant.addMenuItem(nachos);
        restaurant.addMenuItem(flan);

        createStandardMeals(restaurant, Arrays.asList(tacos, burrito, nachos, flan));
    }

    private static void createFrenchMenu(Restaurant restaurant) {
        MenuItem coqAuVin = new MenuItem("Coq au Vin", 26.99, MAIN_DISH, STANDARD, false);
        MenuItem onionSoup = new MenuItem("French Onion Soup", 12.99, STARTER, VEGETARIAN, false);
        MenuItem ratatouille = new MenuItem("Ratatouille", 18.99, MAIN_DISH, VEGETARIAN, true);
        MenuItem cremeBrulee = new MenuItem("Crème Brûlée", 9.99, DESSERT, VEGETARIAN, true);

        restaurant.addMenuItem(coqAuVin);
        restaurant.addMenuItem(onionSoup);
        restaurant.addMenuItem(ratatouille);
        restaurant.addMenuItem(cremeBrulee);

        createStandardMeals(restaurant, Arrays.asList(coqAuVin, onionSoup, ratatouille, cremeBrulee));
    }

    private static void createStandardMeals(Restaurant restaurant, List<MenuItem> items) {
        // Get items by category
        List<MenuItem> starters = items.stream()
            .filter(item -> item.getCategory() == STARTER)
            .collect(Collectors.toList());
        List<MenuItem> mainDishes = items.stream()
            .filter(item -> item.getCategory() == MAIN_DISH)
            .collect(Collectors.toList());
        List<MenuItem> desserts = items.stream()
            .filter(item -> item.getCategory() == DESSERT)
            .collect(Collectors.toList());

        // Create half meal (Starter + Main)
        if (!starters.isEmpty() && !mainDishes.isEmpty()) {
            restaurant.createMeal(
                "Special Combo 1",
                Arrays.asList(starters.get(0), mainDishes.get(0)), // Starter + Main
                MealType.STANDARD,
                HALF_MEAL,
                true
            );
        }

        // Create full meal (Starter + Main + Dessert)
        if (!starters.isEmpty() && !mainDishes.isEmpty() && !desserts.isEmpty()) {
            restaurant.createMeal(
                "Special Combo 2",
                Arrays.asList(starters.get(0), mainDishes.get(0), desserts.get(0)), // Starter + Main + Dessert
                MealType.STANDARD,
                FULL_MEAL,
                false
            );
        }

        // Find vegetarian items by category
        List<MenuItem> vegetarianStarters = items.stream()
            .filter(item -> item.getType() == VEGETARIAN && item.getCategory() == STARTER)
            .collect(Collectors.toList());
        List<MenuItem> vegetarianMains = items.stream()
            .filter(item -> item.getType() == VEGETARIAN && item.getCategory() == MAIN_DISH)
            .collect(Collectors.toList());

        // Create vegetarian half meal (Starter + Main)
        if (!vegetarianStarters.isEmpty() && !vegetarianMains.isEmpty()) {
            restaurant.createMeal(
                "Light Combo",
                Arrays.asList(
                    vegetarianStarters.get(0),
                    vegetarianMains.get(0)
                ), // Vegetarian starter + main
                MealType.VEGETARIAN,
                HALF_MEAL,
                false
            );
        }

        // Create full meal with all categories
        if (!starters.isEmpty() && !mainDishes.isEmpty() && !desserts.isEmpty()) {
            restaurant.createMeal(
                "Family Feast",
                Arrays.asList(
                    starters.get(0),
                    mainDishes.get(0),
                    desserts.get(0)
                ), // One of each category
                MealType.STANDARD,
                FULL_MEAL,
                true
            );
        }
    }

    private static void initializeCouriers(MyFoodoraSystem system) {
        Courier courier1 = new Courier("Bob", "Smith", new Coordinate(40.7125, -74.0063), "555-0123", "courier1", "Courier123!");
        Courier courier2 = new Courier("Alice", "Johnson", new Coordinate(40.7580, -73.9855), "555-0124", "courier2", "Courier456!");

        courier1.setOnDuty(true);
        courier2.setOnDuty(true);

        system.addUser(courier1);
        system.addUser(courier2);
        LOGGER.info("Couriers initialized and set on duty");
    }

    private static void initializeCustomers(MyFoodoraSystem system) {
        // Create 7 customers with different notification preferences
        List<Customer> customers = new ArrayList<>();
        
        customers.add(new Customer("Mike", "Wilson", new Coordinate(40.7130, -74.0065), "mike@email.com", "123-456-7890", "customer1", "Cust123!"));
        customers.add(new Customer("Sarah", "Brown", new Coordinate(40.7135, -74.0060), "sarah@email.com", "123-456-7891", "customer2", "Cust456!"));
        customers.add(new Customer("David", "Lee", new Coordinate(40.7140, -74.0070), "david@email.com", "123-456-7892", "customer3", "Cust789!"));
        customers.add(new Customer("Emma", "Davis", new Coordinate(40.7145, -74.0075), "emma@email.com", "123-456-7893", "customer4", "Cust012!"));
        customers.add(new Customer("James", "Taylor", new Coordinate(40.7150, -74.0080), "james@email.com", "123-456-7894", "customer5", "Cust345!"));
        customers.add(new Customer("Lisa", "Anderson", new Coordinate(40.7155, -74.0085), "lisa@email.com", "123-456-7895", "customer6", "Cust678!"));
        customers.add(new Customer("Tom", "Martin", new Coordinate(40.7160, -74.0090), "tom@email.com", "123-456-7896", "customer7", "Cust901!"));

        // Set different notification preferences
        for (int i = 0; i < customers.size(); i++) {
            Customer customer = customers.get(i);
            // Enable notifications for first 5 customers
            if (i < 5) {
                customer.enableNotification();
            }
            system.addUser(customer);
        }

        LOGGER.info("Customers initialized with varying notification preferences");
    }

    private static void sendInitialNotifications(MyFoodoraSystem system) {
        // Get all restaurants
        List<Restaurant> restaurants = system.getUsers().stream()
            .filter(user -> user instanceof Restaurant)
            .map(user -> (Restaurant) user)
            .toList();

        // Send welcome notifications for each restaurant's meal of the week
        for (Restaurant restaurant : restaurants) {
            system.notifySpecialOffer(restaurant, "Welcome to " + restaurant.getName() + "! Check out our special meals!");
        }

        LOGGER.info("Initial notifications sent to subscribed customers");
    }

    private static void logSystemStatus(MyFoodoraSystem system) {
        LOGGER.info("System Status:");
        LOGGER.info("Total users: " + system.getUsers().size());
        LOGGER.info("Managers: " + system.getUsers().stream().filter(u -> u instanceof Manager).count());
        LOGGER.info("Restaurants: " + system.getUsers().stream().filter(u -> u instanceof Restaurant).count());
        LOGGER.info("Couriers: " + system.getUsers().stream().filter(u -> u instanceof Courier).count());
        LOGGER.info("Customers: " + system.getUsers().stream().filter(u -> u instanceof Customer).count());
    }
}
