public class Courier extends User{

    private String surname;
    private Coordinate location;
    private String phoneNumber;
    private int deliveredOrders;
    private boolean onDuty;

    public Courier(String name, String surname, Coordinate location, String phoneNumber, String username, String password) {
        super(IDGenerator.generateID("CR"), name, username, password);
        this.id = IDGenerator.generateID("CR");
        this.surname = surname;
        this.location = location;
        this.phoneNumber = phoneNumber;
        this.deliveredOrders = 0;
        this.onDuty = false;  // Default to off-duty
    }

    // Getters
    public boolean isOnDuty() { 
        return onDuty; 
    }

    public int getDeliveredOrders() { 
        return deliveredOrders; 
    }

    public Coordinate getLocation() {
        return location;
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
    public void completeDelivery() {
        this.deliveredOrders++;
    }

    // Courier response to delivery request
    public boolean acceptDeliveryCall() {
        //어떻게 해야하는지 모르겠어요
        return true;
    }

    public boolean refuseDeliveryCall() {
        //어떻게 해야하는지 모르겠어요
        return true;
    }
}
