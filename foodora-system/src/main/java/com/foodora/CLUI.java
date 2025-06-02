package com.foodora;

import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Command Line User Interface (CLUI) for the MyFoodora system.
 * This class handles all command-line interactions and manages the system state.
 * It provides functionality for user authentication, restaurant management,
 * order processing, and system configuration.
 */
public class CLUI {
    // Data structures to store system state
    private static Map<String, User> users = new HashMap<>();  // Maps usernames to User objects
    private static Map<String, Restaurant> restaurants = new HashMap<>();  // Maps restaurant names to Restaurant objects
    private static Map<String, Customer> customers = new HashMap<>();  // Maps usernames to Customer objects
    private static Map<String, Courier> couriers = new HashMap<>();  // Maps usernames to Courier objects
    private static Map<String, Order> activeOrders = new HashMap<>();  // Maps order IDs to Order objects
    private static Map<String, String> restaurantUsernames = new HashMap<>();  // Maps usernames to restaurant names

    // Current session state
    private static String currentUser = null;
    private static UserType currentUserType = null;

    // System configuration
    private static DeliveryPolicy deliveryPolicy = DeliveryPolicy.FASTEST;
    private static ProfitPolicy profitPolicy = ProfitPolicy.TARGETED;
    private static double serviceFee = 5.0;
    private static double markupPercentage = 0.1;
    private static double deliveryCost = 3.0;

    // Enums for system state
    private enum UserType {
        MANAGER, RESTAURANT, CUSTOMER, COURIER
    }

    // Enums for policies
    public enum DeliveryPolicy {
        FASTEST, FAIR
    }

    public enum ProfitPolicy {
        TARGETED, MARKUP
    }

    static {
        // Initialize CEO user
        users.put("ceo", new User("ceo", "123456789", UserType.MANAGER));
    }

    /**
     * Resets the system state to its initial configuration.
     * Clears all users, orders, and credentials except for the default manager account.
     * This method is primarily used for testing purposes.
     */
    public static void resetState() {
        users.clear();
        restaurants.clear();
        customers.clear();
        couriers.clear();
        activeOrders.clear();
        currentUser = null;
        currentUserType = null;
        restaurantUsernames.clear();
        // Initialize CEO user
        users.put("ceo", new User("ceo", "123456789", UserType.MANAGER));
    }

    /**
     * Executes a command with the given arguments.
     * 
     * @param command The command to execute (case-insensitive)
     * @param args Array of arguments for the command
     * @throws Exception if the command is invalid or execution fails
     */
    public static void executeCommand(String command, String[] args) throws Exception {
        command = command.toLowerCase();
        
        switch (command) {
            case "login":
                handleLogin(args);
                break;
            case "logout":
                handleLogout(args);
                break;
            case "setup":
                handleSetup(args);
                break;
            case "registerrestaurant":
                handleRegisterRestaurant(args);
                break;
            case "registercustomer":
                handleRegisterCustomer(args);
                break;
            case "registercourier":
                handleRegisterCourier(args);
                break;
            case "adddishrestaurantmenu":
            case "adddishrestauarantmenu": // Handle both spellings
                handleAddDishToMenu(args);
                break;
            case "createmeal":
                handleCreateMeal(args);
                break;
            case "adddish2meal":
                handleAddDishToMeal(args);
                break;
            case "showmeal":
                handleShowMeal(args);
                break;
            case "savemeal":
                handleSaveMeal(args);
                break;
            case "setspecialoffer":
                handleSetSpecialOffer(args);
                break;
            case "removefromspecialoffer":
                handleRemoveFromSpecialOffer(args);
                break;
            case "createorder":
                handleCreateOrder(args);
                break;
            case "additem2order":
                handleAddItemToOrder(args);
                break;
            case "endorder":
                handleEndOrder(args);
                break;
            case "onduty":
                handleOnDuty(args);
                break;
            case "offduty":
                handleOffDuty(args);
                break;
            case "finddeliverer":
                handleFindDeliverer(args);
                break;
            case "setdeliverypolicy":
                handleSetDeliveryPolicy(args);
                break;
            case "setprofitpolicy":
                handleSetProfitPolicy(args);
                break;
            case "associatecard":
                handleAssociateCard(args);
                break;
            case "showcourierdeliveries":
                handleShowCourierDeliveries();
                break;
            case "showrestauranttop":
                handleShowRestaurantTop();
                break;
            case "showcustomers":
                handleShowCustomers();
                break;
            case "showmenuitem":
                handleShowMenuItem(args);
                break;
            case "showtotalprofit":
                if (args.length == 2) {
                    handleShowTotalProfitInRange(args);
                } else {
                    handleShowTotalProfit();
                }
                break;
            case "help":
                handleHelp();
                break;
            default:
                throw new Exception("Unknown command: " + command);
        }
    }

    /**
     * Handles user login with the provided credentials.
     * 
     * @param args Array containing username and password
     * @throws Exception if credentials are invalid or missing
     */
    private static String handleLogin(String[] args) throws Exception {
        if (args.length != 2) {
            throw new Exception("Invalid arguments. Usage: login <username> <password>");
        }
        String username = args[0];
        String password = args[1];

        if (!users.containsKey(username)) {
            throw new Exception("Invalid credentials");
        }

        User user = users.get(username);
        if (!user.password.equals(password)) {
            throw new Exception("Invalid credentials");
        }

        currentUser = username;
        currentUserType = user.type;
        return "Logged in as " + username;
    }

    /**
     * Handles user logout. Clears the current user session.
     */
    private static String handleLogout(String[] args) throws Exception {
        if (args.length != 0) {
            throw new Exception("Invalid arguments. Usage: logout");
        }
        if (currentUser == null) {
            throw new Exception("Not logged in");
        }
        currentUser = null;
        currentUserType = null;
        return "Logged out successfully";
    }

    private static void handleSetup(String[] args) throws Exception {
        if (currentUserType != UserType.MANAGER) {
            throw new Exception("Only managers can perform setup");
        }

        if (args.length != 3) {
            throw new Exception("Setup requires number of restaurants, customers, and couriers");
        }

        int numRestaurants = Integer.parseInt(args[0]);
        int numCustomers = Integer.parseInt(args[1]);
        int numCouriers = Integer.parseInt(args[2]);

        // Create sample data
        for (int i = 0; i < numRestaurants; i++) {
            String name = "Restaurant" + i;
            String username = "rest" + i;
            String password = "pass" + i;
            String location = (Math.random() * 100) + "," + (Math.random() * 100);
            handleRegisterRestaurant(new String[]{name, location, username, password});
        }

        System.out.println("System setup with " + numRestaurants + " restaurants");
    }

    private static String handleRegisterRestaurant(String[] args) throws Exception {
        if (args.length != 4) {
            throw new Exception("Invalid arguments. Usage: registerRestaurant <name> <location> <username> <password>");
        }
        String name = args[0];
        String location = args[1];
        String username = args[2];
        String password = args[3];

        if (restaurants.containsKey(name)) {
            throw new Exception("Restaurant already exists");
        }
        if (users.containsKey(username)) {
            throw new Exception("Username already exists");
        }

        restaurants.put(name, new Restaurant(name, location));
        users.put(username, new User(username, password, UserType.RESTAURANT));
        restaurantUsernames.put(username, name);  // Map username to restaurant name
        return "Registered restaurant: " + name;
    }

    private static String handleRegisterCustomer(String[] args) throws Exception {
        if (args.length != 5) {
            throw new Exception("Invalid arguments. Usage: registerCustomer <firstName> <lastName> <username> <address> <password>");
        }
        String firstName = args[0];
        String lastName = args[1];
        String username = args[2];
        String address = args[3];
        String password = args[4];

        if (users.containsKey(username)) {
            throw new Exception("Username already exists");
        }

        customers.put(username, new Customer(firstName, lastName, address));
        users.put(username, new User(username, password, UserType.CUSTOMER));
        return "Registered customer: " + firstName + " " + lastName;
    }

    private static String handleRegisterCourier(String[] args) throws Exception {
        if (args.length != 5) {
            throw new Exception("Invalid arguments. Usage: registerCourier <firstName> <lastName> <username> <position> <password>");
        }
        String firstName = args[0];
        String lastName = args[1];
        String username = args[2];
        String position = args[3];
        String password = args[4];

        if (users.containsKey(username)) {
            throw new Exception("Username already exists");
        }

        couriers.put(username, new Courier(firstName, lastName, position));
        users.put(username, new User(username, password, UserType.COURIER));
        return "Registered courier: " + firstName + " " + lastName;
    }

    private static void handleAddDishToMenu(String[] args) throws Exception {
        if (currentUserType != UserType.RESTAURANT) {
            throw new Exception("Only restaurants can add dishes to menu");
        }

        if (args.length != 4) {
            throw new Exception("Adding dish requires: <dishName> <category (starter/main/dessert)> <foodType (standard/vegetarian/gluten-free)> <price>");
        }

        String dishName = removeQuotes(args[0]);
        String category = removeQuotes(args[1]).toLowerCase();
        String foodType = removeQuotes(args[2]).toLowerCase();
        double price;
        
        try {
            price = Double.parseDouble(removeQuotes(args[3]));
        } catch (NumberFormatException e) {
            throw new Exception("Invalid price format. Must be a number");
        }

        // Validate category
        if (!Arrays.asList("starter", "main", "dessert").contains(category)) {
            throw new Exception("Invalid category. Must be one of: starter, main, dessert");
        }

        // Validate food type
        if (!Arrays.asList("standard", "vegetarian", "gluten-free").contains(foodType)) {
            throw new Exception("Invalid food type. Must be one of: standard, vegetarian, gluten-free");
        }

        Restaurant restaurant = restaurants.get(currentUser);
        Dish dish = new Dish(dishName, category, foodType, price);
        restaurant.menu.put(dishName, dish);
        System.out.println("Added dish to menu: " + dishName);
    }

    private static void handleCreateMeal(String[] args) throws Exception {
        if (currentUserType != UserType.RESTAURANT) {
            throw new Exception("Only restaurants can create meals");
        }

        if (args.length != 1) {
            throw new Exception("Creating meal requires meal name");
        }

        String mealName = args[0];
        Restaurant restaurant = restaurants.get(currentUser);
        
        if (restaurant.meals.containsKey(mealName)) {
            throw new Exception("Meal already exists");
        }

        Meal meal = new Meal(mealName);
        restaurant.meals.put(mealName, meal);
    }

    private static void handleAddDishToMeal(String[] args) throws Exception {
        if (currentUserType != UserType.RESTAURANT) {
            throw new Exception("Only restaurants can add dishes to meals");
        }

        if (args.length != 2) {
            throw new Exception("Adding dish to meal requires dish name and meal name");
        }

        String dishName = args[0];
        String mealName = args[1];

        Restaurant restaurant = restaurants.get(currentUser);
        if (!restaurant.meals.containsKey(mealName)) {
            throw new Exception("Meal not found");
        }
        if (!restaurant.menu.containsKey(dishName)) {
            throw new Exception("Dish not found in menu");
        }

        Meal meal = restaurant.meals.get(mealName);
        meal.dishes.add(restaurant.menu.get(dishName));
    }

    private static void handleShowMeal(String[] args) throws Exception {
        if (currentUserType != UserType.RESTAURANT) {
            throw new Exception("Only restaurants can view meals");
        }

        if (args.length != 1) {
            throw new Exception("ShowMeal requires meal name");
        }

        String mealName = args[0];
        Restaurant restaurant = restaurants.get(currentUser);
        Meal meal = restaurant.meals.get(mealName);
        
        if (meal == null) {
            throw new Exception("Meal not found: " + mealName);
        }

        System.out.println("Meal: " + mealName);
        for (Dish dish : meal.dishes) {
            System.out.println("  - " + dish.name + " (" + dish.category + ", " + dish.foodType + ") $" + dish.price);
        }
    }

    private static void handleSaveMeal(String[] args) throws Exception {
        if (currentUserType != UserType.RESTAURANT) {
            throw new Exception("Only restaurants can save meals");
        }

        if (args.length != 1) {
            throw new Exception("Saving meal requires meal name");
        }

        String mealName = args[0];
        Restaurant restaurant = restaurants.get(currentUser);
        
        if (!restaurant.meals.containsKey(mealName)) {
            throw new Exception("Meal not found");
        }
    }

    private static void handleSetSpecialOffer(String[] args) throws Exception {
        if (currentUserType != UserType.RESTAURANT) {
            throw new Exception("Only restaurants can set special offers");
        }

        if (args.length != 1) {
            throw new Exception("Setting special offer requires meal name");
        }

        String mealName = args[0];
        Restaurant restaurant = restaurants.get(currentUser);
        
        if (!restaurant.meals.containsKey(mealName)) {
            throw new Exception("Meal not found");
        }

        restaurant.meals.get(mealName).isSpecialOffer = true;
    }

    private static void handleRemoveFromSpecialOffer(String[] args) throws Exception {
        if (currentUserType != UserType.RESTAURANT) {
            throw new Exception("Only restaurants can remove special offers");
        }

        if (args.length != 1) {
            throw new Exception("RemoveFromSpecialOffer requires meal name");
        }

        String mealName = args[0];
        Restaurant restaurant = restaurants.get(currentUser);
        Meal meal = restaurant.meals.get(mealName);
        
        if (meal == null) {
            throw new Exception("Meal not found: " + mealName);
        }

        meal.isSpecialOffer = false;
        System.out.println("Removed special offer from meal: " + mealName);
    }

    private static void handleCreateOrder(String[] args) throws Exception {
        if (currentUserType != UserType.CUSTOMER) {
            throw new Exception("Only customers can create orders");
        }

        if (args.length != 2) {
            throw new Exception("CreateOrder requires: <restaurantName> <orderID>");
        }

        String restaurantName = removeQuotes(args[0]);
        String orderId = removeQuotes(args[1]);

        // Find restaurant by name
        Restaurant targetRestaurant = null;
        String restaurantUsername = null;
        for (Map.Entry<String, Restaurant> entry : restaurants.entrySet()) {
            if (entry.getValue().name.equals(restaurantName)) {
                targetRestaurant = entry.getValue();
                restaurantUsername = entry.getKey();
                break;
            }
        }

        if (targetRestaurant == null) {
            throw new Exception("Restaurant not found: " + restaurantName);
        }

        if (activeOrders.containsKey(orderId)) {
            throw new Exception("Order ID already exists: " + orderId);
        }

        Order order = new Order(restaurantUsername, orderId);
        activeOrders.put(orderId, order);
        System.out.println("Created order: " + orderId + " at restaurant: " + restaurantName);
    }

    private static String handleAddItemToOrder(String[] args) throws Exception {
        if (args.length != 2) {
            throw new Exception("Invalid arguments. Usage: addItem2Order <orderId> <itemName>");
        }
        if (!activeOrders.containsKey(args[0])) {
            throw new Exception("Order not found: " + args[0]);
        }
        Order order = activeOrders.get(args[0]);
        Restaurant restaurant = null;
        for (Restaurant r : restaurants.values()) {
            if (r.name.equals(order.restaurantName)) {
                restaurant = r;
                break;
            }
        }
        if (restaurant == null) {
            throw new Exception("Restaurant not found: " + order.restaurantName);
        }
        if (!restaurant.menu.containsKey(args[1])) {
            throw new Exception("Item not found in restaurant menu: " + args[1]);
        }
        order.items.add(restaurant.menu.get(args[1]));
        return "Added item to order: " + args[1];
    }

    private static void handleEndOrder(String[] args) throws Exception {
        if (currentUserType != UserType.CUSTOMER) {
            throw new Exception("Only customers can end orders");
        }

        if (args.length != 2) {
            throw new Exception("EndOrder requires order ID and date (YYYY-MM-DD)");
        }

        String orderId = removeQuotes(args[0]);
        String date = removeQuotes(args[1]);

        if (!activeOrders.containsKey(orderId)) {
            throw new Exception("Order not found: " + orderId);
        }

        Order order = activeOrders.get(orderId);
        order.date = date;
        System.out.println("Order completed: " + orderId);
    }

    private static void handleOnDuty(String[] args) throws Exception {
        if (currentUserType != UserType.COURIER) {
            throw new Exception("Only couriers can change duty status");
        }

        if (args.length != 1) {
            throw new Exception("Setting duty status requires courier username");
        }

        String username = args[0];
        if (!couriers.containsKey(username)) {
            throw new Exception("Courier not found");
        }

        couriers.get(username).onDuty = true;
    }

    private static void handleOffDuty(String[] args) throws Exception {
        if (currentUserType != UserType.COURIER) {
            throw new Exception("Only couriers can change duty status");
        }

        if (args.length != 1) {
            throw new Exception("Setting duty status requires courier username");
        }

        String username = args[0];
        if (!couriers.containsKey(username)) {
            throw new Exception("Courier not found");
        }

        couriers.get(username).onDuty = false;
    }

    private static void handleSetDeliveryPolicy(String[] args) throws Exception {
        if (currentUserType != UserType.MANAGER) {
            throw new Exception("Only managers can set delivery policy");
        }

        if (args.length != 1) {
            throw new Exception("SetDeliveryPolicy requires policy name");
        }

        String policy = removeQuotes(args[0]).toUpperCase();
        try {
            deliveryPolicy = DeliveryPolicy.valueOf(policy);
            System.out.println("Delivery policy set to: " + policy);
        } catch (IllegalArgumentException e) {
            throw new Exception("Invalid delivery policy. Must be one of: FASTEST, FAIR");
        }
    }

    private static void handleSetProfitPolicy(String[] args) throws Exception {
        if (currentUserType != UserType.MANAGER) {
            throw new Exception("Only managers can set profit policy");
        }

        if (args.length != 1) {
            throw new Exception("SetProfitPolicy requires policy name");
        }

        String policy = removeQuotes(args[0]).toUpperCase();
        try {
            profitPolicy = ProfitPolicy.valueOf(policy);
            System.out.println("Profit policy set to: " + policy);
        } catch (IllegalArgumentException e) {
            throw new Exception("Invalid profit policy. Must be one of: TARGETED, MARKUP");
        }
    }

    private static void handleShowCourierDeliveries() throws Exception {
        if (currentUserType != UserType.MANAGER) {
            throw new Exception("Only managers can view courier deliveries");
        }

        for (Map.Entry<String, Courier> entry : couriers.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue().deliveries + " deliveries");
        }
    }

    private static void handleShowRestaurantTop() throws Exception {
        if (currentUserType != UserType.MANAGER) {
            throw new Exception("Only managers can view restaurant rankings");
        }

        // In a real implementation, we would sort restaurants by orders/revenue
        for (Map.Entry<String, Restaurant> entry : restaurants.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue().name);
        }
    }

    private static void handleShowCustomers() throws Exception {
        if (currentUserType != UserType.MANAGER) {
            throw new Exception("Only managers can view customer list");
        }

        for (Map.Entry<String, Customer> entry : customers.entrySet()) {
            Customer customer = entry.getValue();
            System.out.println(entry.getKey() + ": " + customer.firstName + " " + customer.lastName);
        }
    }

    private static void handleShowTotalProfit() throws Exception {
        if (currentUserType != UserType.MANAGER) {
            throw new Exception("Only managers can view total profit");
        }

        // In a real implementation, we would calculate actual profit
        System.out.println("Total profit: " + String.format("%.2f", 0.0));
    }

    private static void handleShowTotalProfitInRange(String[] args) throws Exception {
        if (currentUserType != UserType.MANAGER) {
            throw new Exception("Only managers can view profit");
        }

        if (args.length != 2) {
            throw new Exception("ShowTotalProfit with date range requires: <startDate (YYYY-MM-DD)> <endDate (YYYY-MM-DD)>");
        }

        try {
            LocalDate startDate = parseDate(args[0]);
            LocalDate endDate = parseDate(args[1]);
            
            // Calculate profit for orders within date range
            double totalProfit = 0.0;
            for (Order order : activeOrders.values()) {
                if (order.date != null) {
                    LocalDate orderDate = parseDate(order.date);
                    if (!orderDate.isBefore(startDate) && !orderDate.isAfter(endDate)) {
                        double orderPrice = calculateOrderPrice(order);
                        totalProfit += orderPrice * markupPercentage + serviceFee - deliveryCost;
                    }
                }
            }

            System.out.printf("Total profit between %s and %s: %.2f%n", startDate, endDate, totalProfit);
        } catch (Exception e) {
            throw new Exception("Error calculating profit: " + e.getMessage());
        }
    }

    private static void handleHelp() {
        System.out.println("Available commands:");
        System.out.println("  login <username> <password>");
        System.out.println("  logout");
        System.out.println("  setup <numRestaurants> <numCustomers> <numCouriers>");
        System.out.println("  registerRestaurant <name> <location> <username> <password>");
        System.out.println("  registerCustomer <firstName> <lastName> <username> <address> <password>");
        System.out.println("  registerCourier <firstName> <lastName> <username> <position> <password>");
        System.out.println("  addDishRestaurantMenu <dishName> <category> <foodType> <unitPrice>");
        System.out.println("  createMeal <mealName>");
        System.out.println("  addDish2Meal <dishName> <mealName>");
        System.out.println("  showMeal <mealName>");
        System.out.println("  saveMeal <mealName>");
        System.out.println("  setSpecialOffer <mealName>");
        System.out.println("  removeFromSpecialOffer <mealName>");
        System.out.println("  createOrder <restaurantName> <orderName>");
        System.out.println("  addItem2Order <orderName> <itemName>");
        System.out.println("  endOrder <orderName> <date>");
        System.out.println("  onDuty <username>");
        System.out.println("  offDuty <username>");
        System.out.println("  findDeliverer <orderName>");
        System.out.println("  setDeliveryPolicy <policy>");
        System.out.println("  setProfitPolicy <policy>");
        System.out.println("  associateCard <username> <cardType>");
        System.out.println("  showCourierDeliveries");
        System.out.println("  showRestaurantTop");
        System.out.println("  showCustomers");
        System.out.println("  showMenuItem <restaurantName>");
        System.out.println("  showTotalProfit");
        System.out.println("  showTotalProfit <startDate> <endDate>");
        System.out.println("  help");
    }

    private static void handleFindDeliverer(String[] args) throws Exception {
        if (currentUserType != UserType.RESTAURANT) {
            throw new Exception("Only restaurants can find deliverers");
        }

        if (args.length != 1) {
            throw new Exception("FindDeliverer requires order ID");
        }

        String orderId = args[0];
        Order order = activeOrders.get(orderId);
        
        if (order == null) {
            throw new Exception("Order not found: " + orderId);
        }

        // Find available courier based on delivery policy
        Courier selectedCourier = null;
        if (deliveryPolicy == DeliveryPolicy.FASTEST) {
            // Select courier closest to restaurant
            double minDistance = Double.MAX_VALUE;
            for (Map.Entry<String, Courier> entry : couriers.entrySet()) {
                Courier courier = entry.getValue();
                if (courier.onDuty) {
                    // Calculate distance (simplified)
                    double distance = calculateDistance(courier.position, restaurants.get(order.restaurantName).location);
                    if (distance < minDistance) {
                        minDistance = distance;
                        selectedCourier = courier;
                    }
                }
            }
        } else {
            // Select courier with least deliveries
            int minDeliveries = Integer.MAX_VALUE;
            for (Map.Entry<String, Courier> entry : couriers.entrySet()) {
                Courier courier = entry.getValue();
                if (courier.onDuty && courier.deliveries < minDeliveries) {
                    minDeliveries = courier.deliveries;
                    selectedCourier = courier;
                }
            }
        }

        if (selectedCourier == null) {
            throw new Exception("No available couriers found");
        }

        System.out.println("Selected courier: " + selectedCourier.firstName + " " + selectedCourier.lastName);
    }

    private static void handleShowMenuItem(String[] args) throws Exception {
        if (args.length != 1) {
            throw new Exception("ShowMenuItem requires restaurant name");
        }

        String restaurantName = removeQuotes(args[0]);
        Restaurant restaurant = null;
        
        // Find restaurant by name
        for (Restaurant r : restaurants.values()) {
            if (r.name.equals(restaurantName)) {
                restaurant = r;
                break;
            }
        }
        
        if (restaurant == null) {
            throw new Exception("Restaurant not found: " + restaurantName);
        }

        System.out.println("Menu for " + restaurantName + ":");
        for (Dish dish : restaurant.menu.values()) {
            System.out.println("  - " + dish.name + " (" + dish.category + ", " + dish.foodType + ") $" + dish.price);
        }
    }

    private static String handleAssociateCard(String[] args) throws Exception {
        if (args.length != 2) {
            throw new Exception("Invalid arguments. Usage: associateCard <username> <cardType>");
        }
        if (!customers.containsKey(args[0])) {
            throw new Exception("Customer not found: " + args[0]);
        }
        if (!Arrays.asList("basic", "points", "lottery").contains(args[1].toLowerCase())) {
            throw new Exception("Invalid card type. Must be one of: basic, points, lottery");
        }
        Customer customer = customers.get(args[0]);
        if (customer.cardType != null && customer.cardType.equals(args[1])) {
            throw new Exception("Card type already associated with customer");
        }
        customer.cardType = args[1];
        return "Associated " + args[1] + " card with customer: " + args[0];
    }

    private static double calculateDistance(String pos1, String pos2) {
        String[] p1 = pos1.split(",");
        String[] p2 = pos2.split(",");
        double x1 = Double.parseDouble(p1[0]);
        double y1 = Double.parseDouble(p1[1]);
        double x2 = Double.parseDouble(p2[0]);
        double y2 = Double.parseDouble(p2[1]);
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    private static double calculateOrderPrice(Order order) {
        double total = 0.0;
        for (Dish item : order.items) {
            total += item.price;
        }
        return total;
    }

    /**
     * Removes surrounding quotes from a string if present.
     * 
     * @param str The string to process
     * @return String with quotes removed, or null if input is null
     */
    private static String removeQuotes(String str) {
        if (str == null) return null;
        return str.replaceAll("^\"|\"$", "");
    }

    /**
     * Parses a date string in YYYY-MM-DD format.
     * 
     * @param dateStr The date string to parse
     * @return LocalDate object representing the parsed date
     * @throws Exception if the date string is null or in invalid format
     */
    private static LocalDate parseDate(String dateStr) throws Exception {
        if (dateStr == null) {
            throw new Exception("Date cannot be null");
        }
        dateStr = removeQuotes(dateStr);
        try {
            return LocalDate.parse(dateStr);
        } catch (Exception e) {
            throw new Exception("Invalid date format. Use YYYY-MM-DD format");
        }
    }

    // Inner classes for data models
    public static class Restaurant {
        public String name;
        public String location;
        public Map<String, Dish> menu = new HashMap<>();
        public Map<String, Meal> meals = new HashMap<>();

        public Restaurant(String name, String location) {
            this.name = name;
            this.location = location;
        }
    }

    public static class Customer {
        public String firstName;
        public String lastName;
        public String address;
        public String cardType;

        public Customer(String firstName, String lastName, String address) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.address = address;
        }
    }

    public static class Courier {
        public String firstName;
        public String lastName;
        public String position;
        public boolean onDuty;
        public int deliveries;

        public Courier(String firstName, String lastName, String position) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.position = position;
            this.onDuty = true;
            this.deliveries = 0;
        }
    }

    public static class Dish {
        public String name;
        public String category;
        public String foodType;
        public double price;

        public Dish(String name, String category, String foodType, double price) {
            if (price < 0) {
                throw new IllegalArgumentException("Price cannot be negative");
            }
            if (!Arrays.asList("starter", "main", "dessert").contains(category.toLowerCase())) {
                throw new IllegalArgumentException("Invalid category. Must be one of: starter, main, dessert");
            }
            if (!Arrays.asList("standard", "vegetarian", "gluten-free").contains(foodType.toLowerCase())) {
                throw new IllegalArgumentException("Invalid food type. Must be one of: standard, vegetarian, gluten-free");
            }
            this.name = name;
            this.category = category;
            this.foodType = foodType;
            this.price = price;
        }
    }

    public static class Meal {
        public String name;
        public List<Dish> dishes = new ArrayList<>();
        public boolean isSpecialOffer;

        public Meal(String name) {
            this.name = name;
            this.isSpecialOffer = false;
        }
    }

    public static class Order {
        public String restaurantName;
        public String orderId;
        public List<Dish> items = new ArrayList<>();
        public String date;

        public Order(String restaurantName, String orderId) {
            this.restaurantName = restaurantName;
            this.orderId = orderId;
        }
    }

    private static class User {
        String username;
        String password;
        UserType type;

        User(String username, String password, UserType type) {
            this.username = username;
            this.password = password;
            this.type = type;
        }
    }
}