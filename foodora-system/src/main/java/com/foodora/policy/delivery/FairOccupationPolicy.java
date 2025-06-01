package com.foodora.policy.delivery;

import com.foodora.user.Courier;
import com.foodora.model.Order;
import java.util.List;
import java.util.Comparator;

/**
 * A delivery policy that aims to distribute orders fairly among couriers.
 * It selects the courier with the least number of delivered orders to ensure
 * a balanced workload distribution.
 */
public class FairOccupationPolicy implements DeliveryPolicy {

    @Override
    public Courier selectCourier(List<Courier> couriers, Order order) {
        if (couriers == null || couriers.isEmpty() || order == null) {
            return null;
        }

        return couriers.stream()
            .filter(Courier::isOnDuty)
            .min(Comparator.comparingInt(Courier::getDeliveredOrders))
            .orElse(null);
    }
}
