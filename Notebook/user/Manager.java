import java.util.*;

public class Manager extends User{

    private String surname;

    // Configuration settings
    private double serviceFeePercentage = 0;
    private double markupPercentage = 0;
    private double deliveryCost = 0;

    // Registered users
    private List<Restaurant> restaurants = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();
    private List<Courier> couriers = new ArrayList<>();

    public Manager(String name, String surname, String username, String password) {
        super(IDGenerator.generateID("R"), name, username, password);
        this.surname = surname;
    }

    // Authentication
    public boolean authenticate(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    // --- User management ---
    public void addRestaurant(Restaurant r) {
        restaurants.add(r);
    }

    public void removeRestaurant(Restaurant r) {
        restaurants.remove(r);
    }

    public void activateRestaurant(Restaurant r) {
        r.setActive(true);
    }

    public void deactivateRestaurant(Restaurant r) {
        r.setActive(false);
    }

    public void addCustomer(Customer c) {
        customers.add(c);
    }

    public void removeCustomer(Customer c) {
        customers.remove(c);
    }

    public void activateCustomer(Customer c) {
        c.setActive(true);
    }

    public void deactivateCustomer(Customer c) {
        c.setActive(false);
    }

    public void addCourier(Courier c) {
        couriers.add(c);
    }

    public void removeCourier(Courier c) {
        couriers.remove(c);
    }

    public void activateCourier(Courier c) {
        c.setActive(true);
    }

    public void deactivateCourier(Courier c) {
        c.setActive(false);
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
        // Implement: total of all customer payments minus costs
        return 0.0;
    }

    public double computeTotalProfit(List<Order> orders) {
        // Implement: income - delivery cost - item costs
        return 0.0;
    }

    public double computeAverageIncomePerCustomer(List<Order> orders) {
        Set<Customer> activeCustomers = new HashSet<>();
        double totalIncome = 0;

        for (Order o : orders) {
            totalIncome += o.getTotalPrice();
            activeCustomers.add(o.getCustomer());
        }

        return activeCustomers.isEmpty() ? 0 : totalIncome / activeCustomers.size();
    }

    // determine service fee/markup percentage/delivery cost to meet a target profit
    public void determineServiceFee(ServiceFee servicefee){
        
    }

    public void determineDeliveryPolicy(DeliveryPolicy policy) {
        // Set a delivery policy (e.g., fastest courier, closest courier)
        // Implement policy pattern or strategy pattern if needed
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

    // Getters (optional)
    public double getServiceFeePercentage() {
        return serviceFeePercentage;
    }

    public double getMarkupPercentage() {
        return markupPercentage;
    }

    public double getDeliveryCost() {
        return deliveryCost;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
}
