package com.foodora.policy.delivery;

import com.foodora.user.Courier;
import com.foodora.model.Order;
import com.foodora.util.Coordinate;
import java.util.List;
import java.util.Objects;

/**
 * A delivery policy that selects the courier who can deliver the order in the shortest time.
 * This is calculated based on the total distance the courier needs to travel:
 * 1. Distance from courier's current location to the restaurant
 * 2. Distance from restaurant to customer's location
 */
public class FastestDeliveryPolicy implements DeliveryPolicy {

    @Override
    public Courier selectCourier(List<Courier> couriers, Order order) {
        if (couriers == null || couriers.isEmpty() || order == null) {
            return null;
        }

        Courier bestCourier = null;
        double shortestDistance = Double.MAX_VALUE;

        Coordinate restaurantLoc = order.getRestaurant().getLocation();
        Coordinate customerLoc = order.getCustomer().getAddress();

        // Validate locations
        if (restaurantLoc == null || customerLoc == null) {
            return null;
        }

        for (Courier courier : couriers) {
            if (!courier.isOnDuty() || courier.getLocation() == null) {
                continue;
            }

            double totalDistance = calculateTotalDistance(
                courier.getLocation(),
                restaurantLoc,
                customerLoc
            );

            if (totalDistance < shortestDistance) {
                shortestDistance = totalDistance;
                bestCourier = courier;
            }
        }

        return bestCourier;
    }

    /**
     * Calculates the total distance a courier needs to travel for delivery.
     * @param courierLoc Courier's current location
     * @param restaurantLoc Restaurant's location
     * @param customerLoc Customer's location
     * @return Total distance to be traveled
     */
    private double calculateTotalDistance(Coordinate courierLoc, Coordinate restaurantLoc, Coordinate customerLoc) {
        double distToRestaurant = distanceBetween(courierLoc, restaurantLoc);
        double distToCustomer = distanceBetween(restaurantLoc, customerLoc);
        return distToRestaurant + distToCustomer;
    }

    /**
     * Calculates Euclidean distance between two coordinates.
     */
    private double distanceBetween(Coordinate a, Coordinate b) {
        Objects.requireNonNull(a, "Coordinate a cannot be null");
        Objects.requireNonNull(b, "Coordinate b cannot be null");
        
        double dx = a.getX() - b.getX();
        double dy = a.getY() - b.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }
}
