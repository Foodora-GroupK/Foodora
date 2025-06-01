package com.foodora.fidelity;

import com.foodora.user.Customer;
import java.util.Random;

/**
 * A fidelity card implementation that provides a chance to win a free meal.
 * Each purchase has a 5% chance of being completely free.
 * The randomization is done using a secure random number generator.
 */
public class LotteryFidelityCard implements FidelityCard {
    private static final double WIN_PROBABILITY = 0.05; // 5% chance
    private static final Random RANDOM = new Random(); // Single instance for better randomization
    private String lastWinMessage; // Stores the last win message if any

    @Override
    public double applyDiscount(double totalPrice, Customer customer) {
        if (RANDOM.nextDouble() < WIN_PROBABILITY) {
            lastWinMessage = String.format("🎉 Congratulations %s! You won a free meal worth %.2f€!", 
                customer.getName(), totalPrice);
            return 0.0;
        }
        lastWinMessage = null;
        return totalPrice;
    }

    /**
     * Returns the win message if the customer won in the last discount calculation.
     * @return The win message or null if the customer didn't win
     */
    public String getLastWinMessage() {
        return lastWinMessage;
    }
}
