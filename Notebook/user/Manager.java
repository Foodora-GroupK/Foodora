import java.util.*;
import java.util.stream.Collectors;

public class Manager extends User{

    private String surname;
 

    public Manager(String name, String surname, String username, String password) {
        super(IDGenerator.generateID("M"), name, username, password);
        this.surname = surname;
    }

    // Authentication
    public boolean authenticate(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    public void addUser(User user) {
        MyFoodoraSystem.getInstance().addUser(user);
    }

    public void removeUser(User user) {
        MyFoodoraSystem.getInstance().removeUser(user);
    }

    public void activateUser(User user) {
        user.setActive(true);
    }

    public void deactivateUser(User user) {
        user.setActive(false);
    }
    
    // --- Configuration settings ---
    public void setServiceFeePercentage(double serviceFee) {
        MyFoodoraSystem system = MyFoodoraSystem.getInstance();
        system.setFees(serviceFee, system.getMarkupPercentage(), system.getDeliveryCost());
    }

    public void setMarkupPercentage(double markup) {
        MyFoodoraSystem system = MyFoodoraSystem.getInstance();
        system.setFees(system.getServiceFee(), markup, system.getDeliveryCost());
    }

    public void setDeliveryCost(double deliveryCost) {
        MyFoodoraSystem system = MyFoodoraSystem.getInstance();
        system.setFees(system.getServiceFee(), system.getMarkupPercentage(), deliveryCost);
    }

    // --- Statistics and business metrics (skeletons) ---
    public double computeTotalIncome(List<Order> orders) {
        return MyFoodoraSystem.getInstance().computeTotalIncome();
    }

    public double computeTotalProfit(List<Order> orders) {
        return MyFoodoraSystem.getInstance().computeTotalProfit();
    }

    public double computeAverageIncomePerCustomer(List<Order> orders) {
        return MyFoodoraSystem.getInstance().computeAvgIncomePerCustomer();
    }

    public void optimizeForTargetProfit(double targetProfit) {
        MyFoodoraSystem.getInstance().optimizeForTargetProfit(targetProfit);
    }

    public void determineDeliveryPolicy(DeliveryPolicy policy) {
        MyFoodoraSystem.getInstance().setDeliveryPolicy(policy);
    }

    public Restaurant getMostSellingRestaurant(List<Order> orders) {
        Map<Restaurant, Integer> salesCount = new HashMap<>();
        
        // Count orders per restaurant
        for (Order order : orders) {
            Restaurant restaurant = order.getRestaurant();
            salesCount.put(restaurant, salesCount.getOrDefault(restaurant, 0) + 1);
        }

        // Find restaurant with maximum sales
        return salesCount.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(null);
    }

    public Restaurant getLeastSellingRestaurant(List<Order> orders) {
        Map<Restaurant, Integer> salesCount = new HashMap<>();
        
        // Get all restaurants from the system
        Set<Restaurant> allRestaurants = MyFoodoraSystem.getInstance().getUsers().stream()
            .filter(u -> u instanceof Restaurant)
            .map(u -> (Restaurant) u)
            .collect(Collectors.toSet());
            
        // Initialize count for all restaurants to 0
        allRestaurants.forEach(r -> salesCount.put(r, 0));
        
        // Count orders per restaurant
        for (Order order : orders) {
            Restaurant restaurant = order.getRestaurant();
            salesCount.put(restaurant, salesCount.getOrDefault(restaurant, 0) + 1);
        }

        // Find restaurant with minimum sales
        return salesCount.entrySet().stream()
            .min(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(null);
    }

    public Courier getMostActiveCourier(List<Order> orders) {
        Map<Courier, Integer> deliveryCount = new HashMap<>();
        
        // Count deliveries per courier
        for (Order order : orders) {
            Courier courier = order.getCourier();
            if (courier != null) {
                deliveryCount.put(courier, deliveryCount.getOrDefault(courier, 0) + 1);
            }
        }

        // Find courier with maximum deliveries
        return deliveryCount.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(null);
    }

    public Courier getLeastActiveCourier(List<Order> orders) {
        Map<Courier, Integer> deliveryCount = new HashMap<>();
        
        // Get all couriers from the system
        Set<Courier> allCouriers = MyFoodoraSystem.getInstance().getUsers().stream()
            .filter(u -> u instanceof Courier)
            .map(u -> (Courier) u)
            .collect(Collectors.toSet());
            
        // Initialize count for all couriers to 0
        allCouriers.forEach(c -> deliveryCount.put(c, 0));
        
        // Count deliveries per courier
        for (Order order : orders) {
            Courier courier = order.getCourier();
            if (courier != null) {
                deliveryCount.put(courier, deliveryCount.getOrDefault(courier, 0) + 1);
            }
        }

        // Find courier with minimum deliveries
        return deliveryCount.entrySet().stream()
            .min(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(null);
    }
}