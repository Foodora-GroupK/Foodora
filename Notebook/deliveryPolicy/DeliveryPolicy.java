public interface DeliveryPolicy {
    Courier selectCourier(List<Courier> couriers, Order order);
}
