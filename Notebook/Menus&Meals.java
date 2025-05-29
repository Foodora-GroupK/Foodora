import jave.util.List;
import java.util.ArrayList;

public class MenuItem {
    public enum Category {STARTER, MAIN_DISH, DESSERT}
    public enum Type {STANDARD, VEGETARIAN}

    private String name;
    private double price;
    private Category category;
    private Type type;
    private boolean isGlutenFree;
    
    public Menu(String name, double price, Category category, Type type, boolean is GlutenFree) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.type = type;
        this.isGlutenFree = isGlutenFree;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public Type getType() {
        return type;
    }

    public boolean isGlutenFree() {
        return isGlutenFree;
    }

}

public class Menu {
    private List<MenuItem> starters;
    private List<MenuItem> mainDishes;
    private List<MenuItem> desserts;

    public Menu() {
        this.starters = new ArrayList<>();
        this.mainDishes = new ArrayList<>();
        this.desserts = new ArrayList<>();
    }

    public void addItem(MenuItem item) {
        switch (item.getCategory()) {
            case STARTER -> starters.add(item);
            case MAIN_DISH -> mainDishes.add(item);
            case DESSERT -> desserts.add(item);
        }
    }

    public void removeItem(MenuItem item) {
        switch (item.getCategory()) {
            case STARTER -> starters.remove(item);
            case MAIN_DISH -> mainDishes.remove(item);
            case DESSERT -> desserts.remove(item);
        }
    }

    public List<MenuItem> getStarters() {
        return starters;
    }

    public List<MenuItem> getMainDishes() {
        return mainDishes;
    }

    public List<MenuItem> getDesserts() {
        return desserts;
    }
}

public class Meal {
    public enum MealType {STANDARD, VEGETARIAN, GLUTEN_FREE}
    public enum MealSize {HALF_MEAL, FULL_MEAL}

    private List<MenuItem> items;
    private MealType mealType;
    private MealSize mealSize;
    private boolean is_meal_of_the_week;
    private double discountFactor;

    public Meal(List<MenuItem> items, MealType mealType, MealSize mealSize, boolean is_meal_of_the_week, double discountFactor) {
        this.items = items;
        this.mealType = mealType;
        this.mealSize = mealSize;
        this.is_meal_of_the_week = is_meal_of_the_week;
        this.discountFactor = (discountFactor != null) ? discountFactor : 0.10;

        validateMealComposition();
    }

    private void validateMealComposition() {
        // validate item count based on meal size
        if (mealSize = MealSize.HALF_MEAL && items.size() !=2) {
            throw new IllegalArgumetException("Half-meal must have 2 items");
        }else if (mealSize == MealSize.FULL_MEAL && items.size() != 3) {
            throw new IllegalArgumetException("Full-meal must have 3 items.");
        }

        // validate meal type consistency
        for (MenuItem item : items){
            if (mealType == MealType.VEGETARIAN && item.getType() != MenuItem.Type.VEGETARIAN) {
                throw new IllegalArgumetException("Vegetarian meal contains non-vegetarian item.");
            } else if (mealType = MealType.GLUTEN_FREE && !item.isGlutenFree()) {
                throw new IllegalArgumetException("Gluten-free meal contains item with gluten")
            }
        }

        // validate structure
        long num_starters = items.stream().filter(i -> i.getCategory() == MenuItem.Category.STARTER).count();
        long mum_mains = items.stream().filter(i -> i.getCategory() == MenuItem.Category.MAIN_DISH).count();
        long num_desserts = items.stream().filter(i -> i.getCategory() == MenuItem.Category.DESSERT).count();

        if (mealSize == MealSize.HALF_MEAL) {
            if (!((num_starters == 1 && num_mains == 1) || (num_mains == 1 && num_desserts == 1))) {
                throw new IllegalArgumentException("Half-meal must be starter+main or main+dessert.");
            }
        } else if (mealSize == MealSize.FULL_MEAL) {
            if (!(num_starters == 1 && num_mains == 1 && num_desserts == 1)) {
                throw new IllegalArgumentException("Full-meal must have one starter, one main, one dessert.");
            }
        }
    }

    public double getPrice() {
        double total = 0;
        for (MenuItem item : items) {
            total += item.getPrice()
        }
        double discount = is_meal_of_the_week ? discountFactor : 0.05;
        return total * (1-discount);
    }
}



