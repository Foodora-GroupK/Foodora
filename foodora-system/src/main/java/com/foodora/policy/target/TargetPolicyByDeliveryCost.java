package com.foodora.policy.target;

import com.foodora.MyFoodoraSystem;
import java.util.Objects;

/**
 * Implementation of target profit policy that adjusts the delivery cost.
 * This policy keeps service fee and markup percentage constant while modifying
 * the delivery cost to achieve the target profit.
 */
public class TargetProfitByDeliveryCost implements TargetProfitPolicy {
    
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
        double serviceFee = system.getServiceFee();

        // Calculate required delivery cost to achieve target profit
        double deliveryCost = avgPrice * markup + serviceFee - (targetProfit / numOrders);
        
        // Validate calculated delivery cost
        if (deliveryCost < 0) {
            throw new IllegalStateException(
                "Cannot achieve target profit: required delivery cost would be negative");
        }

        system.setFees(serviceFee, markup, deliveryCost);
    }
}
