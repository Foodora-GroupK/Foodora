public class FastestDeliveryPolicy implements DeliveryPolicy {

    @Override
    public Courier selectCourier(List<Courier> couriers, Order order) {
        Courier bestCourier = null;
        double shortestDistance = Double.MAX_VALUE;

        Coordinate restaurantLoc = order.getRestaurant().getLocation();
        Coordinate customerLoc = order.getCustomer().getAddress();

        for (Courier courier : couriers) {
            if (!courier.isOnDuty()) continue;

            Coordinate courierLoc = courier.getLocation();

            double distToRestaurant = distanceBetween(courierLoc, restaurantLoc);
            double distToCustomer = distanceBetween(restaurantLoc, customerLoc);
            double totalDistance = distToRestaurant + distToCustomer;

            if (totalDistance < shortestDistance) {
                shortestDistance = totalDistance;
                bestCourier = courier;
            }
        }

        return bestCourier;
    }

    private double distanceBetween(Coordinate a, Coordinate b) {
        double dx = a.getX() - b.getX();
        double dy = a.getY() - b.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }
}
