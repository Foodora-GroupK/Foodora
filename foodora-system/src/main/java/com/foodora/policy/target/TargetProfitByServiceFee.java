package com.foodora.policy.target;

import com.foodora.MyFoodoraSystem;
import java.util.Objects;

/**
 * Implementation of target profit policy that adjusts the service fee.
 * This policy keeps markup percentage and delivery cost constant while modifying
 * the service fee to achieve the target profit.
 */
public class TargetProfitByServiceFee implements TargetProfitPolicy {
    
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
        double markup = system.getMarkupPercentage();
        double deliveryCost = system.getDeliveryCost();

        // Calculate required service fee to achieve target profit
        double serviceFee = (targetProfit / numOrders) + deliveryCost - (avgPrice * markup);
        
        // Validate calculated service fee
        if (serviceFee < 0) {
            throw new IllegalStateException(
                "Cannot achieve target profit: required service fee would be negative");
        }

        system.setFees(serviceFee, markup, deliveryCost);
    }
}
