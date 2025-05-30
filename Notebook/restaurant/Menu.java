import jave.util.List;

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