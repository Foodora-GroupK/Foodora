public class Order {
    private Customer customer;
    private Restaurant restaurant;
    private Courier courier;

    private List<MenuItem> items;
    private List<Meal> meals;

    private double finalPrice;

    public Order(Customer customer, Restaurant restaurant, Courier courier, List<MenuItem> items, List<Meal> meals) {
        this.customer = customer;
        this.restaurant = restaurant;
        this.courier = courier;
        this.items = items != null ? items : new ArrayList<>();
        this.meals = meals != null ? meals : new ArrayList<>();
    }

    public double calculateTotalPrice() {
        double itemTotal = items.stream().mapToDouble(MenuItem::getPrice).sum();
        double mealTotal = meals.stream().mapToDouble(Meal::getPrice).sum();
        return itemTotal + mealTotal;
    }

    // Getters
    public Customer getCustomer() { return customer; }
    public Restaurant getRestaurant() { return restaurant; }
    public Courier getCourier() { return courier; }

    public void setFinalPrice(double finalPrice) {
        this.finalPrice = finalPrice;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public List<MenuItem> getItems() { return items; }
    public List<Meal> getMeals() { return meals; }

    public void setCourier(Courier courier) {
        this.courier = courier;
    }
}
