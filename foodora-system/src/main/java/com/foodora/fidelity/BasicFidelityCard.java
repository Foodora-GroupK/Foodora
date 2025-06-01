package com.foodora.fidelity;

import com.foodora.user.Customer;

public class BasicFidelityCard implements FidelityCard {
    @Override
    public double applyDiscount(double totalPrice, Customer customer) {
        return totalPrice;  // No discount for basic card
    }
} 