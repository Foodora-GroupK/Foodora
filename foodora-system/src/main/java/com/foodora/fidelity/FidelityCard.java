package com.foodora.fidelity;

import com.foodora.user.Customer;

public interface FidelityCard {
    double applyDiscount(double totalPrice, Customer customer);
} 