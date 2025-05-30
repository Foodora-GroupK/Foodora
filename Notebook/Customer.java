import java.util.*;

public class Customer {

    private final String id;
    private String name;
    private String surname;
    private Coordinate address;
    private String email;
    private String phoneNumber;
    private String username;
    private String password;

    private List<Order> orderHistory;
    private FidelityCard fidelityCard;
    private int points;
    private boolean notification;

    public Customer(String name, String surname, Coordinate address, String email, String phoneNumber, String username, String password) {
        this.id = IDGenerator.generateID("C");
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.password = password;

        this.orderHistory = new ArrayList<>();
        this.fidelityCard = null; // Not registered by default
        this.points = 0;
        this.notification = new BasicFidelityCard(); // Default: no notifications
    }

    public boolean authenticate(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    // Place an order and add it to history
    public void placeOrder(Order order) {
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
        this.fidelityCard = new BasicFidelityCard(); //back to basic plan
    }

    public void enableNotification() {
        this.notification = true;
    }

    public void disableNotification() {
        this.notification = false;
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
}