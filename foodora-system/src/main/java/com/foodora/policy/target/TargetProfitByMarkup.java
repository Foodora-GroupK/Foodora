package com.foodora.policy.target;

import com.foodora.MyFoodoraSystem;
import java.util.Objects;

/**
 * Implementation of target profit policy that adjusts the markup percentage.
 * This policy keeps service fee and delivery cost constant while modifying
 * the markup percentage to achieve the target profit.
 */
public class TargetProfitByMarkup implements TargetProfitPolicy {
    
    @Override
    public void apply(MyFoodoraSystem system, double targetProfit) {
        Objects.requireNonNull(system, "System cannot be null");
        if (targetProfit < 0) {
            throw new IllegalArgumentException("Target profit cannot be negative");
        }

        double income = system.computeTotalIncome();
        int numOrders = system.getCompletedOrders().size();
        if (numOrders == 0) {
            throw new IllegalStateException("Cannot compute target profit with no completed orders");
        }

        double avgPrice = income / numOrders;
        double serviceFee = system.getServiceFee();
        double deliveryCost = system.getDeliveryCost();

        // Calculate required markup to achieve target profit
        double markup = ((targetProfit / numOrders) + deliveryCost - serviceFee) / avgPrice;
        
        // Validate calculated markup
        if (markup < 0) {
            throw new IllegalStateException(
                "Cannot achieve target profit: required markup would be negative");
        }

        system.setFees(serviceFee, markup, deliveryCost);
    }
}
