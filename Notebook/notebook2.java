public class Menu {
    public enum Category { STARTER, MAIN_DISH, DESSERT}
    public enum Type { STANDARD, VEGETERIAN}

    private String name;
    private double pricel
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

}




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
    private String name;
    private int uniqueId;
    private String username;
    private String passward;
    private Coordinate location;

}

