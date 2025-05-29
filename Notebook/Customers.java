import java.util.*;

public class Order {
    private static int idCounter = 1;

    private final int id;
    private final Date orderDate;
    private final List<MenuItem> items;
    private final List<Meal> meals;
    private final Restaurant restaurant;

    public Order(List<MenuItem> items, List<Meal> meals, Restaurant restaurant) {
        this.id = idCounter++;
        this.orderDate = new Date();
        this.items = items != null ? items : new ArrayList<>();
        this.meals = meals != null ? meals : new ArrayList<>();
        this.restaurant = restaurant;
    }

    public double getTotalPrice() {
        double total = 0.0;

        for (MenuItem item : items) {
            total += item.getPrice();
        }

        for (Meal meal : meals) {
            total += meal.getPrice(); // Already includes discount logic
        }

        return total;
    }

    public int getId() {
        return id;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public List<MenuItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public List<Meal> getMeals() {
        return Collections.unmodifiableList(meals);
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }
}

public class Customer {
    private static int idCounter = 1;

    private final int id;
    private String name;
    private String surname;
    private Coordinate address;
    private String email;
    private String phoneNumber;
    private String username;
    private String password;

    private List<Order> orderHistory;
    private FidelityCard fidelityCard;
    private boolean allowSpecialOfferNotifications;

    public Customer(String name, String surname, Coordinate address, String email, String phoneNumber, String username, String password) {
        this.id = idCounter++;
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.password = password;

        this.orderHistory = new ArrayList<>();
        this.fidelityCard = null; // Not registered by default
        this.allowSpecialOfferNotifications = false; // Default: no notifications
    }

    public boolean authenticate(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    // Place an order and add it to history
    public void placeOrder(Order order) {
        orderHistory.add(order);
        if (fidelityCard != null) {
            fidelityCard.addPointsFromOrder(order);
        }
    }

    // Register to a fidelity card plan
    public void registerFidelityCard(FidelityCard card) {
        this.fidelityCard = card;
    }

    // Unregister from fidelity card
    public void unregisterFidelityCard() {
        this.fidelityCard = null;
    }

    // Toggle notifications
    public void giveConsensusForNotifications() {
        this.allowSpecialOfferNotifications = true;
    }

    public void removeConsensusForNotifications() {
        this.allowSpecialOfferNotifications = false;
    }

    // Getters
    public int getId() {
        return id;
    }

    public List<Order> getOrderHistory() {
        return Collections.unmodifiableList(orderHistory);
    }

    public boolean isSubscribedToNotifications() {
        return allowSpecialOfferNotifications;
    }

    public FidelityCard getFidelityCard() {
        return fidelityCard;
    }

    public String getUsername() {
        return username;
    }
}
