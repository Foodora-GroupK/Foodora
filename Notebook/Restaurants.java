import java.util.ArrayList;
import jave.util.List;

public class Coordinate {
    private double x;
    private double y;

    public Coordinate(double x, double y) {
        this.x=x;
        this.y=y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}

public class restaurant {
    private static int idCounter = 1;

    private final int id;
    private String name;
    private Coordinate location;
    private String username;
    private String passward;
    
    private Menu menu;
    private List<Meal> meals;

    private double defaultGenericDiscountFactor = 0.05; // default for regular meals
    private double defaultSpecialDiscountFactor = 0.10; // default for meal-of-the-week
    
    public Restaurat(String name, Coordinate location, String username, String password){
        this.id = idCounter++;
        this.name = name;
        this.location = location;
        this.username = username;
        this.passward = passward;
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
    public void createMeal(List<MenuItem> items, Meal.MealType type, Meal.MealSize size, boolean isMealOfTheWeek) {
        double discount = isMealOfTheWeek ? defaultSpecialDiscountFactor : defaultGenericDiscountFactor;
        Meal newMeal = new Meal(items, type, size, isMealOfTheWeek, discount);
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

    // getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Point getLocation() {
        return location;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    // authenticate
    public boolean authenticate(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }
}