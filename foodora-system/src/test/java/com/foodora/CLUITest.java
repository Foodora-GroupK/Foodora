package com.foodora;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;

public class CLUITest {
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private MyFoodoraSystem system;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
        system = MyFoodoraSystem.getInstance();
        system.reset(); // Clear all users and orders
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void testLoginLogout() throws Exception {
        // Test successful login
        CLUI.executeCommand("login", new String[]{"ceo", "123456789"});
        assertTrue(outputStreamCaptor.toString().contains("Logged in as ceo"));

        // Test logout
        CLUI.executeCommand("logout", new String[]{});
        assertTrue(outputStreamCaptor.toString().contains("Logged out successfully"));

        // Test invalid login
        Exception exception = assertThrows(Exception.class, 
            () -> CLUI.executeCommand("login", new String[]{"invalid", "user"}));
        assertEquals("Invalid credentials", exception.getMessage());
    }

    @Test
    void testRegisterRestaurant() throws Exception {
        // Login as manager first
        CLUI.executeCommand("login", new String[]{"ceo", "123456789"});

        // Test restaurant registration
        CLUI.executeCommand("registerrestaurant", 
            new String[]{"LeBistro", "10.5,20.3", "bistro1", "pass123"});
        
        assertTrue(system.getUsers().stream()
            .anyMatch(u -> u.getUsername().equals("bistro1")));

        // Test duplicate restaurant
        Exception exception = assertThrows(Exception.class, 
            () -> CLUI.executeCommand("registerrestaurant", 
                new String[]{"LeBistro", "10.5,20.3", "bistro1", "pass123"}));
        assertEquals("Username already exists", exception.getMessage());
    }

    @Test
    void testRegisterCustomer() throws Exception {
        // Login as manager
        CLUI.executeCommand("login", new String[]{"ceo", "123456789"});

        // Test customer registration
        CLUI.executeCommand("registercustomer", 
            new String[]{"John", "Doe", "john.doe", "5.5,10.2", "custpass1"});
        
        assertTrue(system.getUsers().stream()
            .anyMatch(u -> u.getUsername().equals("john.doe")));

        // Test duplicate customer
        Exception exception = assertThrows(Exception.class, 
            () -> CLUI.executeCommand("registercustomer", 
                new String[]{"John", "Doe", "john.doe", "5.5,10.2", "custpass1"}));
        assertEquals("Username already exists", exception.getMessage());
    }

    @Test
    void testRestaurantMenu() throws Exception {
        // Setup restaurant
        CLUI.executeCommand("login", new String[]{"ceo", "123456789"});
        CLUI.executeCommand("registerrestaurant", 
            new String[]{"LeBistro", "10.5,20.3", "bistro1", "pass123"});
        CLUI.executeCommand("logout", new String[]{});
        CLUI.executeCommand("login", new String[]{"bistro1", "pass123"});

        // Test adding dish to menu
        CLUI.executeCommand("adddishrestaurantmenu", 
            new String[]{"French Onion Soup", "starter", "standard", "8.99"});
        
        // Verify the menu item was added
        assertTrue(system.getUsers().stream()
            .filter(u -> u.getUsername().equals("bistro1"))
            .map(u -> (com.foodora.user.Restaurant) u)
            .findFirst()
            .get()
            .getMenu()
            .getItems()
            .stream()
            .anyMatch(item -> item.getName().equals("French Onion Soup")));
    }

    @Test
    void testOrderProcess() throws Exception {
        // Setup restaurant and customer
        CLUI.executeCommand("login", new String[]{"ceo", "123456789"});
        CLUI.executeCommand("registerrestaurant", 
            new String[]{"LeBistro", "10.5,20.3", "bistro1", "pass123"});
        CLUI.executeCommand("registercustomer", 
            new String[]{"John", "Doe", "john.doe", "5.5,10.2", "custpass1"});
        CLUI.executeCommand("logout", new String[]{});
        
        // Add menu items
        CLUI.executeCommand("login", new String[]{"bistro1", "pass123"});
        CLUI.executeCommand("adddishrestaurantmenu", 
            new String[]{"French Onion Soup", "starter", "standard", "8.99"});
        CLUI.executeCommand("logout", new String[]{});
        
        // Create order as customer
        CLUI.executeCommand("login", new String[]{"john.doe", "custpass1"});

        // Test order creation and completion
        CLUI.executeCommand("createorder", new String[]{"LeBistro", "Order1"});
        CLUI.executeCommand("additem2order", new String[]{"Order1", "French Onion Soup"});
        CLUI.executeCommand("endorder", new String[]{"Order1", "2024-03-20"});
        
        // Verify order was completed
        assertFalse(system.getCompletedOrders().isEmpty());
    }

    @Test
    void testCourierOperations() throws Exception {
        // Setup courier
        CLUI.executeCommand("login", new String[]{"ceo", "123456789"});
        CLUI.executeCommand("registercourier", 
            new String[]{"Mike", "Delivery", "mike.d", "7.5,12.3", "courpass1"});
        CLUI.executeCommand("logout", new String[]{});
        CLUI.executeCommand("login", new String[]{"mike.d", "courpass1"});

        // Test duty status changes
        CLUI.executeCommand("onduty", new String[]{"mike.d"});
        
        // Verify courier is on duty
        assertTrue(system.getUsers().stream()
            .filter(u -> u.getUsername().equals("mike.d"))
            .map(u -> (com.foodora.user.Courier) u)
            .findFirst()
            .get()
            .isOnDuty());

        CLUI.executeCommand("offduty", new String[]{"mike.d"});
        
        // Verify courier is off duty
        assertFalse(system.getUsers().stream()
            .filter(u -> u.getUsername().equals("mike.d"))
            .map(u -> (com.foodora.user.Courier) u)
            .findFirst()
            .get()
            .isOnDuty());
    }

    @Test
    void testManagerOperations() throws Exception {
        // Login as manager
        CLUI.executeCommand("login", new String[]{"ceo", "123456789"});

        // Test policy settings
        CLUI.executeCommand("setdeliverypolicy", new String[]{"fastest"});
        assertTrue(system.getDeliveryPolicy() instanceof com.foodora.policy.delivery.FastestDeliveryPolicy);

        CLUI.executeCommand("setprofitpolicy", new String[]{"targeted"});
        assertTrue(system.getTargetProfitPolicy() instanceof com.foodora.policy.target.TargetProfitByServiceFee);
    }
} 