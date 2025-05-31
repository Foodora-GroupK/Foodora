import java.util.*;

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
    public void setServiceFeePercentage(double percentage) {
        this.serviceFeePercentage = percentage;
    }

    public void setMarkupPercentage(double percentage) {
        this.markupPercentage = percentage;
    }

    public void setDeliveryCost(double cost) {
        this.deliveryCost = cost;
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
        // Map<Restaurant, Integer> salesCount...
        return null;
    }

    public Restaurant getLeastSellingRestaurant(List<Order> orders) {
        return null;
    }

    public Courier getMostActiveCourier(List<Order> orders) {
        return null;
    }

    public Courier getLeastActiveCourier(List<Order> orders) {
        return null;
    }
}