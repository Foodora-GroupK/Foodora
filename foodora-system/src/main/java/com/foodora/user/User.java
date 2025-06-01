package com.foodora.user;

import java.util.Objects;

/**
 * Abstract base class for all users in the Foodora system.
 * Provides common functionality for authentication and user management.
 */
public abstract class User {
    protected final String id;
    protected String name;
    protected String username;
    protected String password;

    public User(String id, String name, String username, String password) {
        this.id = Objects.requireNonNull(id, "ID cannot be null");
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.username = Objects.requireNonNull(username, "Username cannot be null");
        this.password = Objects.requireNonNull(password, "Password cannot be null");
        
        validateCredentials(username, password);
    }

    /**
     * Authenticates a user with the given credentials.
     * @return true if authentication successful, false otherwise
     */
    public boolean authenticate(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    /**
     * Updates the user's password.
     * @throws IllegalArgumentException if the new password is invalid
     */
    public void updatePassword(String currentPassword, String newPassword) {
        if (!authenticate(username, currentPassword)) {
            throw new IllegalArgumentException("Current password is incorrect");
        }
        validatePassword(newPassword);
        this.password = newPassword;
    }

    /**
     * Updates the user's name.
     * @throws IllegalArgumentException if the new name is invalid
     */
    public void updateName(String newName) {
        this.name = Objects.requireNonNull(newName, "Name cannot be null");
    }

    // Validation methods
    protected void validateCredentials(String username, String password) {
        validateUsername(username);
        validatePassword(password);
    }

    protected void validateUsername(String username) {
        if (username.length() < 3) {
            throw new IllegalArgumentException("Username must be at least 3 characters long");
        }
    }

    protected void validatePassword(String password) {
        if (password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters long");
        }
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }
} 