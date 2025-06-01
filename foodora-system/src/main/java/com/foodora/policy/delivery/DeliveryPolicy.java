package com.foodora.policy.delivery;

import com.foodora.user.Courier;
import com.foodora.model.Order;
import java.util.List;

/**
 * Interface defining the strategy for selecting a courier for delivery.
 * Different implementations can use different algorithms to choose the most appropriate courier.
 */
public interface DeliveryPolicy {
    /**
     * Selects the most appropriate courier for an order based on the policy's criteria.
     * @param couriers List of available couriers
     * @param order The order to be delivered
     * @return The selected courier, or null if no suitable courier is found
     */
    Courier selectCourier(List<Courier> couriers, Order order);
}
