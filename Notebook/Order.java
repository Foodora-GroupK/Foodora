public class Order {
    private List<MenuItem> items;
    private double finalPrice;

    public Order(List<MenuItem> items) {
        this.items = items;
    }

    public double calculateTotalPrice() {
        return items.stream().mapToDouble(MenuItem::getPrice).sum();
    }

    public void setFinalPrice(double finalPrice) {
        this.finalPrice = finalPrice;
    }

    public double getFinalPrice() {
        return finalPrice;
    }
}
