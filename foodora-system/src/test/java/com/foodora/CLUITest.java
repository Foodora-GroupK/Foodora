package com.foodora;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;

public class CLUITest {
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
        CLUI.resetState(); // We'll need to add this method
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void testSetup() {
        // Test initial login as manager
        assertDoesNotThrow(() -> CLUI.executeCommand("login", new String[]{"ceo", "123456789"}));
        
        // Test setup command
        assertDoesNotThrow(() -> CLUI.executeCommand("setup", new String[]{"4", "3", "5"}));
        assertTrue(outputStreamCaptor.toString().contains("System setup with 4 restaurants"));
    }

    @Test
    void testLoginLogout() {
        // Test successful login
        assertDoesNotThrow(() -> CLUI.executeCommand("login", new String[]{"ceo", "123456789"}));
        assertTrue(outputStreamCaptor.toString().contains("Logged in as ceo"));

        // Test logout
        assertDoesNotThrow(() -> CLUI.executeCommand("logout", new String[]{}));
        assertTrue(outputStreamCaptor.toString().contains("Logged out successfully"));

        // Test invalid login
        Exception exception = assertThrows(Exception.class, 
            () -> CLUI.executeCommand("login", new String[]{"invalid", "user"}));
        assertEquals("Invalid credentials", exception.getMessage());
    }

    @Test
    void testRegisterRestaurant() {
        // Login as manager first
        assertDoesNotThrow(() -> CLUI.executeCommand("login", new String[]{"ceo", "123456789"}));

        // Test restaurant registration
        assertDoesNotThrow(() -> CLUI.executeCommand("registerrestaurant", 
            new String[]{"LeBistro", "10.5,20.3", "bistro1", "pass123"}));

        // Test duplicate restaurant
        Exception exception = assertThrows(Exception.class, 
            () -> CLUI.executeCommand("registerrestaurant", 
                new String[]{"LeBistro", "10.5,20.3", "bistro1", "pass123"}));
        assertEquals("Restaurant already exists", exception.getMessage());
    }

    @Test
    void testRegisterCustomer() {
        // Login as manager
        assertDoesNotThrow(() -> CLUI.executeCommand("login", new String[]{"ceo", "123456789"}));

        // Test customer registration
        assertDoesNotThrow(() -> CLUI.executeCommand("registercustomer", 
            new String[]{"John", "Doe", "john.doe", "5.5,10.2", "custpass1"}));

        // Test duplicate customer
        Exception exception = assertThrows(Exception.class, 
            () -> CLUI.executeCommand("registercustomer", 
                new String[]{"John", "Doe", "john.doe", "5.5,10.2", "custpass1"}));
        assertEquals("Customer already exists", exception.getMessage());
    }

    @Test
    void testRestaurantMenu() {
        // Setup restaurant
        assertDoesNotThrow(() -> {
            CLUI.executeCommand("login", new String[]{"ceo", "123456789"});
            CLUI.executeCommand("registerrestaurant", 
                new String[]{"LeBistro", "10.5,20.3", "bistro1", "pass123"});
            CLUI.executeCommand("logout", new String[]{});
            CLUI.executeCommand("login", new String[]{"bistro1", "pass123"});
        });

        // Test adding dish to menu
        assertDoesNotThrow(() -> CLUI.executeCommand("adddishrestaurantmenu", 
            new String[]{"French Onion Soup", "starter", "standard", "8.99"}));

        // Test creating meal
        assertDoesNotThrow(() -> {
            CLUI.executeCommand("createmeal", new String[]{"Classic French Dinner"});
            CLUI.executeCommand("adddish2meal", 
                new String[]{"French Onion Soup", "Classic French Dinner"});
            CLUI.executeCommand("savemeal", new String[]{"Classic French Dinner"});
        });

        // Test special offer
        assertDoesNotThrow(() -> CLUI.executeCommand("setspecialoffer", 
            new String[]{"Classic French Dinner"}));
    }

    @Test
    void testOrderProcess() {
        // Setup restaurant and customer
        assertDoesNotThrow(() -> {
            // Register restaurant and add menu items
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
        });

        // Test order creation and completion
        assertDoesNotThrow(() -> {
            CLUI.executeCommand("createorder", new String[]{"LeBistro", "Order1"});
            CLUI.executeCommand("additem2order", new String[]{"Order1", "French Onion Soup"});
            CLUI.executeCommand("endorder", new String[]{"Order1", "2024-03-20"});
        });
    }

    @Test
    void testCourierOperations() {
        // Setup courier
        assertDoesNotThrow(() -> {
            CLUI.executeCommand("login", new String[]{"ceo", "123456789"});
            CLUI.executeCommand("registercourier", 
                new String[]{"Mike", "Delivery", "mike.d", "7.5,12.3", "courpass1"});
            CLUI.executeCommand("logout", new String[]{});
            CLUI.executeCommand("login", new String[]{"mike.d", "courpass1"});
        });

        // Test duty status changes
        assertDoesNotThrow(() -> {
            CLUI.executeCommand("onduty", new String[]{"mike.d"});
            CLUI.executeCommand("offduty", new String[]{"mike.d"});
        });
    }

    @Test
    void testManagerOperations() {
        // Login as manager
        assertDoesNotThrow(() -> CLUI.executeCommand("login", new String[]{"ceo", "123456789"}));

        // Test policy settings
        assertDoesNotThrow(() -> {
            CLUI.executeCommand("setdeliverypolicy", new String[]{"fastest"});
            CLUI.executeCommand("setprofitpolicy", new String[]{"targeted"});
        });

        // Test reports
        assertDoesNotThrow(() -> {
            CLUI.executeCommand("showcourierdeliveries", new String[]{});
            CLUI.executeCommand("showrestauranttop", new String[]{});
            CLUI.executeCommand("showcustomers", new String[]{});
            CLUI.executeCommand("showtotalprofit", new String[]{});
        });
    }
} 