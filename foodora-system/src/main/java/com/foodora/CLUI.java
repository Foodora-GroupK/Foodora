package com.foodora;

import java.util.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import com.foodora.user.*;
import com.foodora.model.*;
import com.foodora.util.Coordinate;
import com.foodora.policy.delivery.*;
import com.foodora.policy.target.*;
import com.foodora.policy.order.*;

/**
 * Command Line User Interface (CLUI) for the MyFoodora system.
 * This class handles all command-line interactions and delegates operations to MyFoodoraSystem.
 */
public class CLUI {
    // Current session state
    private static String currentUser = null;
    private static UserType currentUserType = null;
    
    // Reference to the system
    private static final MyFoodoraSystem system = MyFoodoraSystem.getInstance();

    // Enums for user types
    private enum UserType {
        MANAGER, RESTAURANT, CUSTOMER, COURIER
    }

    static {
        // Initialize CEO user if not exists
        if (system.getUsers().stream().noneMatch(u -> u.getUsername().equals("ceo"))) {
            Manager ceo = new Manager(
                "CEO",
                "Manager",
                "ceo",
                "123456789"
            );
            system.addUser(ceo);
        }
    }

    /**
     * Executes a command with the given arguments.
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
            case "adddishrestauarantmenu":
                handleAddDishToMenu(args);
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
            case "setdeliverypolicy":
                handleSetDeliveryPolicy(args);
                break;
            case "setprofitpolicy":
                handleSetProfitPolicy(args);
                break;
            case "help":
                handleHelp();
                break;
            default:
                throw new Exception("Unknown command: " + command);
        }
    }

    private static String handleLogin(String[] args) throws Exception {
        if (args.length != 2) {
            throw new Exception("Invalid arguments. Usage: login <username> <password>");
        }
        String username = args[0];
        String password = args[1];

        Optional<User> user = system.getUsers().stream()
            .filter(u -> u.getUsername().equals(username))
            .findFirst();

        if (!user.isPresent() || !user.get().authenticate(username, password)) {
            throw new Exception("Invalid credentials");
        }

        currentUser = username;
        if (user.get() instanceof Manager) {
            currentUserType = UserType.MANAGER;
        } else if (user.get() instanceof Restaurant) {
            currentUserType = UserType.RESTAURANT;
        } else if (user.get() instanceof Customer) {
            currentUserType = UserType.CUSTOMER;
        } else if (user.get() instanceof Courier) {
            currentUserType = UserType.COURIER;
        }
        
        return "Logged in as " + username;
    }

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

    private static String handleRegisterRestaurant(String[] args) throws Exception {
        if (args.length != 4) {
            throw new Exception("Invalid arguments. Usage: registerRestaurant <name> <location> <username> <password>");
        }
        String name = removeQuotes(args[0]);
        String location = removeQuotes(args[1]);
        String username = removeQuotes(args[2]);
        String password = removeQuotes(args[3]);

        // Parse location into Coordinate
        String[] coords = location.split(",");
        if (coords.length != 2) {
            throw new Exception("Invalid location format. Use: x,y");
        }
        Coordinate coord = new Coordinate(Double.parseDouble(coords[0]), Double.parseDouble(coords[1]));

        // Check if username already exists
        if (system.getUsers().stream().anyMatch(u -> u.getUsername().equals(username))) {
            throw new Exception("Username already exists");
        }

        // Create and register new restaurant
        Restaurant restaurant = new Restaurant(name, coord, username, password);
        system.addUser(restaurant);
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

        // Parse address into Coordinate
        String[] coords = address.split(",");
        if (coords.length != 2) {
            throw new Exception("Invalid address format. Use: x,y");
        }
        Coordinate coord = new Coordinate(Double.parseDouble(coords[0]), Double.parseDouble(coords[1]));

        if (system.getUsers().stream().anyMatch(u -> u.getUsername().equals(username))) {
            throw new Exception("Username already exists");
        }

        // Create and register new customer
        Customer customer = new Customer(
            firstName,
            lastName,
            coord,
            username + "@foodora.com", // Default email
            "123-456-7890", // Default phone
            username,
            password
        );
        system.addUser(customer);
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

        // Parse position into Coordinate
        String[] coords = position.split(",");
        if (coords.length != 2) {
            throw new Exception("Invalid position format. Use: x,y");
        }
        Coordinate coord = new Coordinate(Double.parseDouble(coords[0]), Double.parseDouble(coords[1]));

        if (system.getUsers().stream().anyMatch(u -> u.getUsername().equals(username))) {
            throw new Exception("Username already exists");
        }

        // Create and register new courier
        Courier courier = new Courier(
            firstName,
            lastName,
            coord,
            "123-456-7890", // Default phone
            username,
            password
        );
        system.addUser(courier);
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

        // Convert category string to MenuItem.Category
        MenuItem.Category menuCategory;
        switch (category) {
            case "starter":
                menuCategory = MenuItem.Category.STARTER;
                break;
            case "main":
                menuCategory = MenuItem.Category.MAIN_DISH;
                break;
            case "dessert":
                menuCategory = MenuItem.Category.DESSERT;
                break;
            default:
                throw new Exception("Invalid category. Must be one of: starter, main, dessert");
        }

        // Convert food type string to MenuItem.Type and gluten-free flag
        MenuItem.Type menuType;
        boolean isGlutenFree = false;
        switch (foodType) {
            case "standard":
                menuType = MenuItem.Type.STANDARD;
                break;
            case "vegetarian":
                menuType = MenuItem.Type.VEGETARIAN;
                break;
            case "gluten-free":
                menuType = MenuItem.Type.STANDARD;
                isGlutenFree = true;
                break;
            default:
                throw new Exception("Invalid food type. Must be one of: standard, vegetarian, gluten-free");
        }

        // Get the current restaurant
        Restaurant restaurant = (Restaurant) system.getUsers().stream()
            .filter(u -> u instanceof Restaurant && u.getUsername().equals(currentUser))
            .findFirst()
            .orElseThrow(() -> new Exception("Restaurant not found"));

        // Create and add the menu item
        MenuItem menuItem = new MenuItem(dishName, price, menuCategory, menuType, isGlutenFree);
        restaurant.addMenuItem(menuItem);
        System.out.println("Added dish to menu: " + dishName);
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
        Restaurant restaurant = (Restaurant) system.getUsers().stream()
            .filter(u -> u instanceof Restaurant && u.getName().equals(restaurantName))
            .findFirst()
            .orElseThrow(() -> new Exception("Restaurant not found: " + restaurantName));

        // Get the current customer
        Customer customer = (Customer) system.getUsers().stream()
            .filter(u -> u instanceof Customer && u.getUsername().equals(currentUser))
            .findFirst()
            .orElseThrow(() -> new Exception("Customer not found"));
        
        // Create new order
        Order order = new Order(customer, restaurant, null, new ArrayList<>(), new ArrayList<>());
        system.placeOrder(order);
        System.out.println("Created order: " + orderId + " at restaurant: " + restaurantName);
    }

    private static String handleAddItemToOrder(String[] args) throws Exception {
        if (args.length != 2) {
            throw new Exception("Invalid arguments. Usage: addItem2Order <orderId> <itemName>");
        }
        
        String orderId = args[0];
        String itemName = args[1];
        
        // Find the order
        Order order = system.getCompletedOrders().stream()
            .filter(o -> o.getOrderId().equals(orderId))
            .findFirst()
            .orElseThrow(() -> new Exception("Order not found: " + orderId));
        
        // Find the menu item
        MenuItem menuItem = order.getRestaurant().getMenu().getItems().stream()
            .filter(item -> item.getName().equals(itemName))
            .findFirst()
            .orElseThrow(() -> new Exception("Item not found in restaurant menu: " + itemName));
        
        order.getItems().add(menuItem);
        return "Added item to order: " + itemName;
    }

    private static void handleEndOrder(String[] args) throws Exception {
        if (currentUserType != UserType.CUSTOMER) {
            throw new Exception("Only customers can end orders");
        }

        if (args.length != 2) {
            throw new Exception("EndOrder requires order ID and date (YYYY-MM-DD)");
        }

        String orderId = removeQuotes(args[0]);
        String dateStr = removeQuotes(args[1]);
        LocalDate date = parseDate(dateStr);

        // Find the order
        Order order = system.getCompletedOrders().stream()
            .filter(o -> o.getOrderId().equals(orderId))
            .findFirst()
            .orElseThrow(() -> new Exception("Order not found: " + orderId));

        order.setStatus(Order.OrderStatus.READY_FOR_DELIVERY);
        system.completeOrder(order);
        
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
        Courier courier = (Courier) system.getUsers().stream()
            .filter(u -> u instanceof Courier && u.getUsername().equals(username))
            .findFirst()
            .orElseThrow(() -> new Exception("Courier not found"));

        courier.setOnDuty(true);
        System.out.println("Courier " + username + " is now on duty");
    }

    private static void handleOffDuty(String[] args) throws Exception {
        if (currentUserType != UserType.COURIER) {
            throw new Exception("Only couriers can change duty status");
        }

        if (args.length != 1) {
            throw new Exception("Setting duty status requires courier username");
        }

        String username = args[0];
        Courier courier = (Courier) system.getUsers().stream()
            .filter(u -> u instanceof Courier && u.getUsername().equals(username))
            .findFirst()
            .orElseThrow(() -> new Exception("Courier not found"));

        courier.setOnDuty(false);
        System.out.println("Courier " + username + " is now off duty");
    }

    private static void handleSetDeliveryPolicy(String[] args) throws Exception {
        if (currentUserType != UserType.MANAGER) {
            throw new Exception("Only managers can set delivery policy");
        }

        if (args.length != 1) {
            throw new Exception("SetDeliveryPolicy requires policy name");
        }

        String policy = removeQuotes(args[0]).toLowerCase();
        DeliveryPolicy newPolicy;
        
        switch (policy) {
            case "fastest":
                newPolicy = new FastestDeliveryPolicy();
                break;
            case "fair":
                newPolicy = new FairOccupationPolicy();
                break;
            default:
                throw new Exception("Invalid delivery policy. Must be one of: fastest, fair");
        }
        
        Manager manager = (Manager) system.getUsers().stream()
            .filter(u -> u instanceof Manager && u.getUsername().equals(currentUser))
            .findFirst()
            .orElseThrow(() -> new Exception("Current user is not a manager"));
            
        manager.determineDeliveryPolicy(newPolicy);
        System.out.println("Delivery policy set to: " + policy);
    }

    private static void handleSetProfitPolicy(String[] args) throws Exception {
        if (currentUserType != UserType.MANAGER) {
            throw new Exception("Only managers can set profit policy");
        }

        if (args.length != 1) {
            throw new Exception("SetProfitPolicy requires policy name");
        }

        String policy = removeQuotes(args[0]).toLowerCase();
        TargetProfitPolicy newPolicy;
        
        switch (policy) {
            case "targeted":
                newPolicy = new TargetProfitByServiceFee();
                break;
            case "markup":
                newPolicy = new TargetProfitByMarkup();
                break;
            default:
                throw new Exception("Invalid profit policy. Must be one of: targeted, markup");
        }
        
        system.setTargetProfitPolicy(newPolicy);
        System.out.println("Profit policy set to: " + policy);
    }

    private static void handleHelp() {
        System.out.println("Available commands:");
        System.out.println("  login <username> <password>");
        System.out.println("  logout");
        System.out.println("  registerRestaurant <name> <location> <username> <password>");
        System.out.println("  registerCustomer <firstName> <lastName> <username> <address> <password>");
        System.out.println("  registerCourier <firstName> <lastName> <username> <position> <password>");
        System.out.println("  addDishRestaurantMenu <dishName> <category> <foodType> <unitPrice>");
        System.out.println("  createOrder <restaurantName> <orderName>");
        System.out.println("  addItem2Order <orderName> <itemName>");
        System.out.println("  endOrder <orderName> <date>");
        System.out.println("  onDuty <username>");
        System.out.println("  offDuty <username>");
        System.out.println("  setDeliveryPolicy <policy>");
        System.out.println("  setProfitPolicy <policy>");
        System.out.println("  help");
    }

    private static String removeQuotes(String str) {
        if (str == null) return null;
        return str.replaceAll("^\"|\"$", "");
    }

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
}