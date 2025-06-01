package com.foodora.policy.target;

import com.foodora.MyFoodoraSystem;

/**
 * Interface for different strategies to achieve a target profit by adjusting various fee parameters.
 * Implementations can modify service fees, markup percentages, or delivery costs to reach the target.
 */
public interface TargetProfitPolicy {
    /**
     * Applies the policy to achieve the target profit by adjusting system parameters.
     * 
     * @param system The MyFoodora system to adjust
     * @param targetProfit The target profit to achieve (must be positive)
     * @throws IllegalArgumentException if targetProfit is negative or system is null
     */
    void apply(MyFoodoraSystem system, double targetProfit);
}
