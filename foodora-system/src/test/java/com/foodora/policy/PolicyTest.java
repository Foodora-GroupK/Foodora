package com.foodora.policy;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import com.foodora.CLUI;
import com.foodora.CLUI.DeliveryPolicy;
import com.foodora.CLUI.ProfitPolicy;

public class PolicyTest {
    
    @BeforeEach
    void setUp() {
        CLUI.resetState();
    }

    @Test
    void testDeliveryPolicyChange() throws Exception {
        // Login as manager
        CLUI.executeCommand("login", new String[]{"ceo", "123456789"});

        // Test setting fastest delivery policy
        assertDoesNotThrow(() -> 
            CLUI.executeCommand("setdeliverypolicy", new String[]{"fastest"}));

        // Test setting fair delivery policy
        assertDoesNotThrow(() -> 
            CLUI.executeCommand("setdeliverypolicy", new String[]{"fair"}));

        // Test invalid policy
        assertThrows(Exception.class, () -> 
            CLUI.executeCommand("setdeliverypolicy", new String[]{"invalid"}));
    }

    @Test
    void testProfitPolicyChange() throws Exception {
        // Login as manager
        CLUI.executeCommand("login", new String[]{"ceo", "123456789"});

        // Test setting targeted profit policy
        assertDoesNotThrow(() -> 
            CLUI.executeCommand("setprofitpolicy", new String[]{"targeted"}));

        // Test setting markup profit policy
        assertDoesNotThrow(() -> 
            CLUI.executeCommand("setprofitpolicy", new String[]{"markup"}));

        // Test invalid policy
        assertThrows(Exception.class, () -> 
            CLUI.executeCommand("setprofitpolicy", new String[]{"invalid"}));
    }

    @Test
    void testDeliveryPolicyImplementation() throws Exception {
        // Login as manager and set up test data
        CLUI.executeCommand("login", new String[]{"ceo", "123456789"});
        
        // Register restaurant
        CLUI.executeCommand("registerrestaurant", 
            new String[]{"TestRest", "10.0,10.0", "rest1", "pass123"});
        
        // Register couriers at different distances
        CLUI.executeCommand("registercourier", 
            new String[]{"Near", "Courier", "near", "11.0,11.0", "pass123"});
        CLUI.executeCommand("registercourier", 
            new String[]{"Far", "Courier", "far", "20.0,20.0", "pass123"});

        // Test fastest delivery policy
        CLUI.executeCommand("setdeliverypolicy", new String[]{"fastest"});
        // Add test for courier selection based on distance

        // Test fair delivery policy
        CLUI.executeCommand("setdeliverypolicy", new String[]{"fair"});
        // Add test for courier selection based on delivery count
    }

    @Test
    void testProfitPolicyImplementation() throws Exception {
        // Login as manager
        CLUI.executeCommand("login", new String[]{"ceo", "123456789"});
        
        // Create test orders with different policies
        CLUI.executeCommand("setprofitpolicy", new String[]{"targeted"});
        // Test order profit calculation with targeted policy

        CLUI.executeCommand("setprofitpolicy", new String[]{"markup"});
        // Test order profit calculation with markup policy
    }

    @Test
    void testUnauthorizedPolicyChange() throws Exception {
        // Test as restaurant
        CLUI.executeCommand("login", new String[]{"ceo", "123456789"});
        CLUI.executeCommand("registerrestaurant", 
            new String[]{"TestRest", "10.0,10.0", "rest1", "pass123"});
        CLUI.executeCommand("logout", new String[]{});
        
        CLUI.executeCommand("login", new String[]{"rest1", "pass123"});
        assertThrows(Exception.class, () -> 
            CLUI.executeCommand("setdeliverypolicy", new String[]{"fastest"}));
        assertThrows(Exception.class, () -> 
            CLUI.executeCommand("setprofitpolicy", new String[]{"targeted"}));
    }

    @Test
    void testPolicyPersistence() throws Exception {
        // Login as manager
        CLUI.executeCommand("login", new String[]{"ceo", "123456789"});
        
        // Set policies
        CLUI.executeCommand("setdeliverypolicy", new String[]{"fastest"});
        CLUI.executeCommand("setprofitpolicy", new String[]{"targeted"});
        
        // Logout and login again
        CLUI.executeCommand("logout", new String[]{});
        CLUI.executeCommand("login", new String[]{"ceo", "123456789"});
        
        // Verify policies persist
        // Note: You would need getter methods in CLUI to properly test this
        // This is a limitation of the current implementation
    }
} 