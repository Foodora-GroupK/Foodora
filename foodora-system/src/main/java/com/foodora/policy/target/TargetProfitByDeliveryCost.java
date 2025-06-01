package com.foodora.policy.target;

import com.foodora.MyFoodoraSystem;

/**
 * A target profit policy that adjusts the delivery cost to meet the target profit.
 * This policy assumes that changing delivery costs is the most effective way to optimize profits.
 */
public class TargetProfitByDeliveryCost implements TargetProfitPolicy {
    private static final double MIN_DELIVERY_COST = 1.0;
    private static final double MAX_DELIVERY_COST = 20.0;
    private static final double ADJUSTMENT_STEP = 0.5;

    @Override
    public void apply(MyFoodoraSystem system, double targetProfit) {
        if (system == null) {
            throw new IllegalArgumentException("System cannot be null");
        }
        if (targetProfit < 0) {
            throw new IllegalArgumentException("Target profit cannot be negative");
        }

        double currentProfit = system.computeTotalProfit();
        double currentDeliveryCost = system.getDeliveryCost();

        // If we're already meeting the target, no changes needed
        if (Math.abs(currentProfit - targetProfit) < 0.01) {
            return;
        }

        // Adjust delivery cost up or down based on current profit
        double newDeliveryCost;
        if (currentProfit < targetProfit) {
            // Need to decrease delivery cost to increase profit
            newDeliveryCost = Math.max(MIN_DELIVERY_COST, currentDeliveryCost - ADJUSTMENT_STEP);
        } else {
            // Need to increase delivery cost to decrease profit
            newDeliveryCost = Math.min(MAX_DELIVERY_COST, currentDeliveryCost + ADJUSTMENT_STEP);
        }

        // Apply the new delivery cost
        system.setFees(system.getServiceFee(), system.getMarkupPercentage(), newDeliveryCost);
    }
} 