package com.foodora.fidelity;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import com.foodora.CLUI;

public class FidelityCardTest {
    
    @BeforeEach
    void setUp() {
        CLUI.resetState();
    }

    @Test
    void testAssociateCard() throws Exception {
        // Login as manager
        CLUI.executeCommand("login", new String[]{"ceo", "123456789"});
        
        // Register a customer
        CLUI.executeCommand("registercustomer", 
            new String[]{"John", "Doe", "john.doe", "10.0,10.0", "pass123"});
        
        // Test associating different card types
        assertDoesNotThrow(() -> 
            CLUI.executeCommand("associatecard", new String[]{"john.doe", "basic"}));
        assertDoesNotThrow(() -> 
            CLUI.executeCommand("associatecard", new String[]{"john.doe", "points"}));
        assertDoesNotThrow(() -> 
            CLUI.executeCommand("associatecard", new String[]{"john.doe", "lottery"}));
    }

    @Test
    void testInvalidCardAssociation() throws Exception {
        // Login as manager
        CLUI.executeCommand("login", new String[]{"ceo", "123456789"});
        
        // Register a customer
        CLUI.executeCommand("registercustomer", 
            new String[]{"John", "Doe", "john.doe", "10.0,10.0", "pass123"});
        
        // Test invalid card type
        assertThrows(Exception.class, () -> 
            CLUI.executeCommand("associatecard", new String[]{"john.doe", "invalid"}));
        
        // Test non-existent customer
        assertThrows(Exception.class, () -> 
            CLUI.executeCommand("associatecard", new String[]{"nonexistent", "basic"}));
    }

    @Test
    void testUnauthorizedCardAssociation() throws Exception {
        // Login as manager and create test users
        CLUI.executeCommand("login", new String[]{"ceo", "123456789"});
        CLUI.executeCommand("registercustomer", 
            new String[]{"John", "Doe", "john.doe", "10.0,10.0", "pass123"});
        CLUI.executeCommand("registerrestaurant", 
            new String[]{"TestRest", "10.0,10.0", "rest1", "pass123"});
        CLUI.executeCommand("logout", new String[]{});
        
        // Test as restaurant (unauthorized)
        CLUI.executeCommand("login", new String[]{"rest1", "pass123"});
        assertThrows(Exception.class, () -> 
            CLUI.executeCommand("associatecard", new String[]{"john.doe", "basic"}));
        
        // Test as customer (unauthorized)
        CLUI.executeCommand("login", new String[]{"john.doe", "pass123"});
        assertThrows(Exception.class, () -> 
            CLUI.executeCommand("associatecard", new String[]{"john.doe", "basic"}));
    }

    @Test
    void testCardPersistence() throws Exception {
        // Login as manager
        CLUI.executeCommand("login", new String[]{"ceo", "123456789"});
        
        // Register customer and associate card
        CLUI.executeCommand("registercustomer", 
            new String[]{"John", "Doe", "john.doe", "10.0,10.0", "pass123"});
        CLUI.executeCommand("associatecard", new String[]{"john.doe", "points"});
        
        // Logout and login again
        CLUI.executeCommand("logout", new String[]{});
        CLUI.executeCommand("login", new String[]{"ceo", "123456789"});
        
        // Try to associate same card type (should fail if card persisted)
        assertThrows(Exception.class, () -> 
            CLUI.executeCommand("associatecard", new String[]{"john.doe", "points"}));
    }

    @Test
    void testCardTypeChange() throws Exception {
        // Login as manager
        CLUI.executeCommand("login", new String[]{"ceo", "123456789"});
        
        // Register customer
        CLUI.executeCommand("registercustomer", 
            new String[]{"John", "Doe", "john.doe", "10.0,10.0", "pass123"});
        
        // Test changing card types
        CLUI.executeCommand("associatecard", new String[]{"john.doe", "basic"});
        assertDoesNotThrow(() -> 
            CLUI.executeCommand("associatecard", new String[]{"john.doe", "points"}));
        assertDoesNotThrow(() -> 
            CLUI.executeCommand("associatecard", new String[]{"john.doe", "lottery"}));
    }
} 