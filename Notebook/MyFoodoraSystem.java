public class MyFoodoraSystem {
    private static MyFoodoraSystem instance;

    // System state
    pricate List<User> users;
    private List<Order> orders;

    private double serviceFee;
    private double markupPercentage;
    private double deliveryCost;

    private DeliveryPolicy deliveryPolicy;

    private MyFoodoraSystem() {
        users = new ArrayList<>();
        orders = new ArrayList<>();
    }

    
}
