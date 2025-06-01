import java.util.*;

public class Customer extends User{

    private String surname;
    private Coordinate address;
    private String email;
    private String phoneNumber;

    private List<Order> orderHistory;
    private FidelityCard fidelityCard;
    private int points;
    private boolean notification;

    public Customer(String name, String surname, Coordinate address, String email, String phoneNumber, String username, String password) {
        super(IDGenerator.generateID("C"), name, username, password);
        this.surname = surname;
        this.address = address;
        this.email = email;
        this.phoneNumber = phoneNumber;

        this.orderHistory = new ArrayList<>();
        this.fidelityCard = new BasicFidelityCard();  // Initialize with BasicFidelityCard by default
        this.points = 0;
        this.notification = false;  // Default: notifications disabled
    }

    public boolean authenticate(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    // Place an order and add it to history
    public void placeOrder(Order order) {
        if (fidelityCard == null) {
            fidelityCard = new BasicFidelityCard();  // Ensure fidelityCard is never null
        }
        double totalPrice = order.calculateTotalPrice();
        double finalPrice = fidelityCard.applyDiscount(totalPrice, this);
        order.setFinalPrice(finalPrice);
        orderHistory.add(order);
        System.out.printf("Order placed. Total: %.2f, Final after discount: %.2f\n", totalPrice, finalPrice);
    }

    // Register to a fidelity card plan
    public void registerFidelityCard(FidelityCard card) {
        this.fidelityCard = card;
    }

    // Unregister from fidelity card
    public void unregisterFidelityCard() {
        this.fidelityCard = new BasicFidelityCard();  // Reset to basic plan
    }

    public void enableNotification() {
        this.notification = true;
    }

    public void disableNotification() {
        this.notification = false;
    }

    public boolean hasNotificationsEnabled() {
        return notification;
    }

    public void notifySpecialOffer(Restaurant restaurant, String message) {
        if (notification) {
            System.out.printf("🔔 Special offer from %s: %s\n", restaurant.getName(), message);
        }
    }

    public void addPoints(int pts) {
        this.points += pts;
    }

    public void deductPoints(int pts) {
        this.points -= pts;
    }

    // Getters
    public List<Order> getOrderHistory() {
        return orderHistory;
    }

    public FidelityCard getFidelityCard() {
        return fidelityCard;
    }

    public int getPoints() {
        return this.points;
    }

    public Coordinate getAddress() {
        return address;
    }
}