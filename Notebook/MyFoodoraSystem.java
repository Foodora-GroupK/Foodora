import java.util.*;

public class MyFoodoraSystem {
    private static MyFoodoraSystem instance;

    // System state
    private List<Manager> managers = new ArrayList<>();
    private List<Restaurant> restaurants = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();
    private List<Courier> couriers = new ArrayList<>();
    private List<Order> completedOrders = new ArrayList<>();

    private double serviceFee = 0;
    private double markupPercentage = 0;
    private double deliveryCost = 0;

    // Delivery policy (Strategy Pattern)
    private DeliveryPolicy deliveryPolicy;

    private MyFoodoraSystem() {
        // Private constructor for Singleton
    }

    public static MyFoodoraSystem getInstance() {
        if (instance == null) {
            instance = new MyFoodoraSystem();
        }
        return instance;
    }

    // === Configuration setters ===
    public void setServiceFee(double fee) {
        this.serviceFee = fee;
    }

    public void setMarkupPercentage(double percentage) {
        this.markupPercentage = percentage;
    }

    public void setDeliveryCost(double cost) {
        this.deliveryCost = cost;
    }

    public void setDeliveryPolicy(DeliveryPolicy policy) {
        this.deliveryPolicy = policy;
    }

    // === Order placement ===
    public void placeOrder(Order order) {
        Courier assignedCourier = deliveryPolicy.assignCourier(order, couriers);
        if (assignedCourier != null) {
            assignedCourier.incrementDeliveredOrders();
            completedOrders.add(order);
            notifySpecialOfferSubscribers(order.getRestaurant().getSpecialOffer());
        }
    }

    // === Notification ===
    public void notifySpecialOfferSubscribers(String offer) {
        for (Customer customer : customers) {
            if (customer.isSubscribedToOffers(
