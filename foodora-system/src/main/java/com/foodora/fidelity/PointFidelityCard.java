package com.foodora.fidelity;

import com.foodora.user.Customer;

/**
 * A fidelity card implementation that works on a points system.
 * Customers earn 1 point for every 10€ spent.
 * When 100 points are accumulated, a 10% discount is applied and the points are deducted.
 */
public class PointFidelityCard implements FidelityCard {
    private static final int POINTS_PER_DISCOUNT = 100;
    private static final double DISCOUNT_RATE = 0.10;
    private static final double POINTS_EARNING_RATE = 10.0; // 1 point per 10€

    @Override
    public double applyDiscount(double totalPrice, Customer customer) {
        // Calculate and add points from this purchase
        int earnedPoints = (int) (totalPrice / POINTS_EARNING_RATE);
        customer.addPoints(earnedPoints);

        // Check if customer has enough points for a discount
        if (customer.getPoints() >= POINTS_PER_DISCOUNT) {
            customer.deductPoints(POINTS_PER_DISCOUNT);
            return totalPrice * (1 - DISCOUNT_RATE); // Apply 10% discount
        }

        return totalPrice;
    }
}
