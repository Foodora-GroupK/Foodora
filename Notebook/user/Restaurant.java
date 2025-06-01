import java.util.ArrayList;
import java.util.List;

public class Restaurant extends User {

    private Coordinate location;
    
    private Menu menu;
    private List<Meal> meals;

    private double defaultGenericDiscountFactor = 0.05; // default for regular meals
    private double defaultSpecialDiscountFactor = 0.10; // default for meal-of-the-week
    
    public Restaurant(String name, Coordinate location, String username, String password) {
        super(IDGenerator.generateID("R"), name, username, password);
        this.location = location;

        this.menu = new Menu();
        this.meals = new ArrayList<>();
    }

    // edit menu
    public void addMenuItem(MenuItem item) {
        menu.addItem(item);
    }

    public void removeMenuItem(MenuItem item) {
        menu.removeItem(item);
    }

    public Menu getMenu() {
        return menu;
    }

    // edit meal
    public void createMeal(String name, List<MenuItem> items, Meal.MealType type, Meal.MealSize size, boolean isMealOfTheWeek) {
        double discount = isMealOfTheWeek ? defaultSpecialDiscountFactor : defaultGenericDiscountFactor;
        Meal newMeal = new Meal(name, items, type, size, isMealOfTheWeek, discount);
        meals.add(newMeal);
    }

    public void removeMeal(Meal meal) {
        meals.remove(meal);
    }

    public List<Meal> getMeals() {
        return meals;
    }

    // discount factors
    public void setDefaultGenericDiscountFactor(double factor) {
        this.defaultGenericDiscountFactor = factor;
    }

    public void setDefaultSpecialDiscountFactor(double factor) {
        this.defaultSpecialDiscountFactor = factor;
    }

    public double getDefaultGenericDiscountFactor() {
        return defaultGenericDiscountFactor;
    }

    public double getDefaultSpecialDiscountFactor() {
        return defaultSpecialDiscountFactor;
    }

    // authenticate
    public boolean authenticate(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    public Coordinate getLocation() {
        return location;
    }
}