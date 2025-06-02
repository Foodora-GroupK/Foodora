package com.foodora.user;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User("test123", "Test User", "testuser", "password123") {
            // Anonymous class since User is abstract
        };
    }

    @Test
    void testUserCreation() {
        assertEquals("test123", testUser.getId());
        assertEquals("Test User", testUser.getName());
        assertEquals("testuser", testUser.getUsername());
        assertTrue(testUser.isActive());
    }

    @Test
    void testInvalidUserCreation() {
        // Test null values
        assertThrows(NullPointerException.class, () -> 
            new User(null, "Test", "test", "pass") {});
        assertThrows(NullPointerException.class, () -> 
            new User("id", null, "test", "pass") {});
        assertThrows(NullPointerException.class, () -> 
            new User("id", "Test", null, "pass") {});
        assertThrows(NullPointerException.class, () -> 
            new User("id", "Test", "test", null) {});

        // Test invalid username (too short)
        assertThrows(IllegalArgumentException.class, () -> 
            new User("id", "Test", "te", "password123") {});

        // Test invalid password (too short)
        assertThrows(IllegalArgumentException.class, () -> 
            new User("id", "Test", "test", "pass") {});
    }

    @Test
    void testAuthentication() {
        assertTrue(testUser.authenticate("testuser", "password123"));
        assertFalse(testUser.authenticate("testuser", "wrongpass"));
        assertFalse(testUser.authenticate("wronguser", "password123"));
    }

    @Test
    void testUpdatePassword() {
        // Test successful password update
        assertDoesNotThrow(() -> 
            testUser.updatePassword("password123", "newpassword123"));
        assertTrue(testUser.authenticate("testuser", "newpassword123"));

        // Test update with incorrect current password
        assertThrows(IllegalArgumentException.class, () -> 
            testUser.updatePassword("wrongpass", "newpass123"));

        // Test update with invalid new password
        assertThrows(IllegalArgumentException.class, () -> 
            testUser.updatePassword("newpassword123", "short"));
    }

    @Test
    void testUpdateName() {
        testUser.updateName("New Name");
        assertEquals("New Name", testUser.getName());
        assertThrows(NullPointerException.class, () -> 
            testUser.updateName(null));
    }

    @Test
    void testActiveStatus() {
        assertTrue(testUser.isActive());
        testUser.setActive(false);
        assertFalse(testUser.isActive());
        testUser.setActive(true);
        assertTrue(testUser.isActive());
    }

    @Test
    void testCredentialValidation() {
        // Test username validation
        assertThrows(IllegalArgumentException.class, () -> 
            testUser.validateUsername("ab"));
        assertDoesNotThrow(() -> 
            testUser.validateUsername("validuser"));

        // Test password validation
        assertThrows(IllegalArgumentException.class, () -> 
            testUser.validatePassword("short"));
        assertDoesNotThrow(() -> 
            testUser.validatePassword("validpass123"));
    }
} 