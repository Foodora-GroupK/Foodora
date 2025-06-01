import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;

import deliveryPolicy.DeliveryPolicy;
import deliveryPolicy.FastestDeliveryPolicy;
import targetProfitPolicy.TargetProfitPolicy;
import targetProfitPolicy.TargetProfitByServiceFee;
import orderSortingPolicy.OrderSortingPolicy;
import orderSortingPolicy.MostOrderedHalfMealPolicy;

public class MyFoodoraSystem {
    private static MyFoodoraSystem instance;

    // System state
    private List<User> users;
    private List<Order> orders;
    private List<Order> completedOrders; 

    private double serviceFee;
    private double markupPercentage;
    private double deliveryCost;

    private DeliveryPolicy deliveryPolicy;
    private TargetProfitPolicy targetProfitPolicy;
    private OrderSortingPolicy orderSortingPolicy;

    private MyFoodoraSystem() {
        // Initialize lists
        users = new ArrayList<>();
        orders = new ArrayList<>();
        completedOrders = new ArrayList<>();

        // Initialize default fees
        serviceFee = 5.0;  // Default service fee
        markupPercentage = 0.1;  // Default 10% markup
        deliveryCost = 10.0;  // Default delivery cost

        // Initialize default policies
        deliveryPolicy = new FastestDeliveryPolicy();  // Default to fastest delivery
        targetProfitPolicy = new TargetProfitByServiceFee();  // Default profit optimization strategy
        orderSortingPolicy = new MostOrderedHalfMealPolicy();  // Default sorting strategy
    }

    // Singleton pattern
    public static MyFoodoraSystem getInstance() {
        if (instance == null) {
            instance = new MyFoodoraSystem();
        }
        return instance;
    }

    // User management
    public void addUser(User user) {
        users.add(user);
    }

    public void removeUser(User user) {
        users.remove(user);
    }

    // set Fees
    public void setFees(double serviceFee, double markupPercentage, double deliveryCost) {
        this.serviceFee = serviceFee;
        this.markupPercentage = markupPercentage;
        this.deliveryCost = deliveryCost;
    }

    // Getters for fees
    public double getServiceFee() { 
        return serviceFee; 
    }

    public double getMarkupPercentage() {
        return markupPercentage; 
    }

    public double getDeliveryCost() { 
        return deliveryCost; 
    }

    // Order management
    public void placeOrder(Order order) {
        // Allocate courier based on current delivery policy
        Courier courier = deliveryPolicy.selectCourier(
            users.stream()
                .filter(u -> u instanceof Courier)
                .map(u -> (Courier) u)
                .collect(Collectors.toList()),
            order
        );
        
        if (courier != null) {
            order.setCourier(courier);
            orders.add(order);
        } else {
            throw new RuntimeException("No available courier found for the order");
        }
    }

    public void completeOrder(Order order) {
        if (orders.remove(order)) {
            completedOrders.add(order);
            if (order.getCourier() != null) {
                order.getCourier().completeDelivery();
            }
        }
    }

    // Notification system
    public void notifySpecialOffer(Restaurant restaurant, String offerMessage) {
        users.stream()
            .filter(u -> u instanceof Customer)
            .map(u -> (Customer) u)
            .filter(Customer::hasNotificationsEnabled)
            .forEach(c -> c.notifySpecialOffer(restaurant, offerMessage));
    }

    // Financial calculations
    public double computeTotalIncome() {
        return completedOrders.stream()
            .mapToDouble(Order::getFinalPrice)
            .sum();
    }

    public double computeProfitForOrder(Order order) {
        return order.getFinalPrice() * markupPercentage + serviceFee - deliveryCost;
    }

    public double computeTotalProfit() {
        return completedOrders.stream()
            .mapToDouble(this::computeProfitForOrder)
            .sum();
    }

    public double computeAvgIncomePerCustomer() {
        Map<Customer, Double> customerIncome = new HashMap<>();
        
        // Sum up income for each customer
        for (Order order : completedOrders) {
            Customer customer = order.getCustomer();
            customerIncome.merge(customer, order.getFinalPrice(), Double::sum);
        }
        
        // If no customers with orders, return 0
        if (customerIncome.isEmpty()) {
            return 0.0;
        }
        
        // Calculate average
        return customerIncome.values().stream()
            .mapToDouble(Double::doubleValue)
            .average()
            .orElse(0.0);
    }

    // Policy management
    public void setDeliveryPolicy(DeliveryPolicy policy) {
        this.deliveryPolicy = policy;
    }

    public void setTargetProfitPolicy(TargetProfitPolicy policy) {
        this.targetProfitPolicy = policy;
    }

    public void optimizeForTargetProfit(double targetProfit) {
        if (targetProfitPolicy != null) {
            targetProfitPolicy.apply(this, targetProfit);
        }
    }

    // to change order sorting policy
    public void setOrderSortingPolicy(OrderSortingPolicy policy) {
        this.orderSortingPolicy = policy;
    }

    // to sort orders
    public void sortOrders() {
        if (orderSortingPolicy != null) {
            orderSortingPolicy.sort(completedOrders);
        }
    }

    // Getters
    public List<Order> getCompletedOrders() {
        return completedOrders;
    }

    public List<User> getUsers() {
        return users;
    }
}
