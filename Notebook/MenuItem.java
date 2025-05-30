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