package com.foodora.user;

import java.util.Objects;

/**
 * Abstract base class for all users in the Foodora system.
 * Provides common functionality for authentication and user management.
 * All user types (Manager, Customer, Restaurant, Courier) inherit from this class.
 */
public abstract class User {
    protected final String id;
    protected String name;
    protected String username;
    protected String password;
    protected boolean active;

    /**
     * Creates a new user with the specified details.
     *
     * @param id Unique identifier for the user
     * @param name User's display name
     * @param username Login username
     * @param password Login password
     * @throws NullPointerException if any parameter is null
     * @throws IllegalArgumentException if username or password are invalid
     */
    public User(String id, String name, String username, String password) {
        this.id = Objects.requireNonNull(id, "ID cannot be null");
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.username = Objects.requireNonNull(username, "Username cannot be null");
        this.password = Objects.requireNonNull(password, "Password cannot be null");
        this.active = true; // Users are active by default
        
        validateCredentials(username, password);
    }

    /**
     * Authenticates a user with the given credentials.
     *
     * @param username The username to verify
     * @param password The password to verify
     * @return true if authentication is successful, false otherwise
     */
    public boolean authenticate(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    /**
     * Updates the user's password after verifying the current password.
     *
     * @param currentPassword The current password for verification
     * @param newPassword The new password to set
     * @throws IllegalArgumentException if current password is incorrect or new password is invalid
     */
    public void updatePassword(String currentPassword, String newPassword) {
        if (!authenticate(username, currentPassword)) {
            throw new IllegalArgumentException("Current password is incorrect");
        }
        validatePassword(newPassword);
        this.password = newPassword;
    }

    /**
     * Updates the user's display name.
     *
     * @param newName The new name to set
     * @throws NullPointerException if newName is null
     */
    public void updateName(String newName) {
        this.name = Objects.requireNonNull(newName, "Name cannot be null");
    }

    /**
     * Sets the active status of the user.
     * Inactive users cannot log in or perform actions in the system.
     *
     * @param active true to activate the user, false to deactivate
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Checks if the user account is currently active.
     *
     * @return true if the user is active, false otherwise
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Validates both username and password.
     *
     * @param username The username to validate
     * @param password The password to validate
     * @throws IllegalArgumentException if either credential is invalid
     */
    protected void validateCredentials(String username, String password) {
        validateUsername(username);
        validatePassword(password);
    }

    /**
     * Validates a username according to system requirements.
     * Username must be at least 3 characters long.
     *
     * @param username The username to validate
     * @throws IllegalArgumentException if username is invalid
     */
    protected void validateUsername(String username) {
        if (username.length() < 3) {
            throw new IllegalArgumentException("Username must be at least 3 characters long");
        }
    }

    /**
     * Validates a password according to system requirements.
     * Password must be at least 6 characters long.
     *
     * @param password The password to validate
     * @throws IllegalArgumentException if password is invalid
     */
    protected void validatePassword(String password) {
        if (password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters long");
        }
    }

    /**
     * Gets the user's unique identifier.
     *
     * @return The user's ID
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the user's display name.
     *
     * @return The user's name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the user's login username.
     *
     * @return The username
     */
    public String getUsername() {
        return username;
    }
} 