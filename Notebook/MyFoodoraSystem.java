import java.util.*;

public abstract class User {
    protected String username;

    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}

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

    // === User registration ===
    public void registerUser(User user) {
        if (user instanceof Manager) {
            managers.add((Manager) user);
        } else if (user instanceof Restaurant) {
            restaurants.add((Restaurant) user);
        } else if (user instanceof Customer) {
            customers.add((Customer) user);
        } else if (user instanceof Courier) {
            couriers.add((Courier) user);
        }
    }

    // === Settings ===
    public void setFees(double serviceFee, double markupPercentage, double deliveryCost) {
        this.serviceFee = serviceFee;
        this.markupPercentage = markupPercentage;
        this.deliveryCost = deliveryCost;
    }

    // === Courier allocation ===
    public Courier allocateCourier() {
        return couriers.isEmpty() ? null : couriers.get(0); // Simplified
    }

    // === Order placement ===
    public void placeOrder(Customer customer, Restaurant restaurant, double price) {
        Courier courier = allocateCourier();
        if (courier == null) {
            System.out.println("No available courier.");
            return;
        }
        Order order = new Order(customer, restaurant, courier, price);
        completedOrders.add(order);
        System.out.println("Order placed by " + customer.getUsername() +
                           " assigned to " + courier.getUsername());
    }

    // === Notifications ===
    public void notifySpecialOffer(String restaurantName) {
        for (Customer customer : customers) {
            if (customer.isSubscribedToOffers()) {
                System.out.println("Notifying " + customer.getUsername() +
                                   " about new offer from " + restaurantName);
            }
        }
    }

    // === Income & Profit ===
    public double computeTotalIncome() {
        return completedOrders.stream().mapToDouble(Order::getPrice).sum();
    }

    public double computeTotalProfit() {
        double profit = 0;
        for (Order order : completedOrders) {
            profit += order.getPrice() * markupPercentage + serviceFee - deliveryCost;
        }
        return profit;
    }

    // === Profit policy selection ===
    public void chooseProfitPolicy(String policyName) {
        System.out.println("Selected profit policy: " + policyName + " (implementation pending)");
    }
}