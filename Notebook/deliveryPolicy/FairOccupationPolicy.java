public class FairOccupationPolicy implements DeliveryPolicy {

    @Override
    public Courier selectCourier(List<Courier> couriers, Order order) {
        Courier leastBusy = null;
        int minDeliveries = Integer.MAX_VALUE;

        for (Courier courier : couriers) {
            if (!courier.isOnDuty()) continue;

            if (courier.getDeliveredOrders() < minDeliveries) {
                minDeliveries = courier.getDeliveredOrders();
                leastBusy = courier;
            }
        }

        return leastBusy;
    }
}
