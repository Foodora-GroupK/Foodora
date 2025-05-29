public class Courier {
    private static int idCounter = 1;

    private final int id;
    private String name;
    private String surname;
    private Coordinate location;
    private String phoneNumber;
    private int deliveredOrders;
    private String username;
    private String password;
    private boolean onDuty;

    public Courier(String name, String surname, Coordinate location, String phoneNumber, String username, String password) {
        this.id = idCounter++;
        this.name = name;
        this.surname = surname;
        this.position = position;
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.password = password;
        this.deliveredOrders = 0;
        this.onDuty = false;  // Default to off-duty
    }

    // Getters
    public int getId() { 
        return id; 
    }

    public String getName() {
        return name; 
    }

    public String getSurname() { 
        return surname; 
    }

    public Point getLocation() { 
        return location; 
    }

    public boolean isOnDuty() { 
        return onDuty; 
    }

    public int getDeliveredOrders() { 
        return deliveredOrders; 
    }

    public String getUsername() { 
        return username; 
    }

    // Authentication method
    public boolean authenticate(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    // Set duty status
    public void setOnDuty(boolean onDuty) {
        this.onDuty = onDuty;
    }

    // Update current position
    public void updateLocation(Coordinate newLocation) {
        this.location = newLocation;
    }

    // Call when a delivery is completed
    public void increseDeliveredOrders() {
        this.deliveredOrders++;
    }

    // Courier response to delivery request
    public boolean acceptDeliveryCall() {
        return true;
    }

    public boolean refuseDeliveryCall() {
        return true;
    }
}
