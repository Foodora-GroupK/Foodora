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