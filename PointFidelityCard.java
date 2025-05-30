public class PointFidelityCard implements FidelityCard {
    private static final int POINTS_PER_DISCOUNT = 100;
    private static final double DISCOUNT_RATE = 0.10;

    @Override
    public double applyDiscount(double totalPrice, Customer customer) {
        int earnedPoints = (int) (totalPrice / 10);
        customer.addPoints(earnedPoints);

        if (customer.getPoints() >= POINTS_PER_DISCOUNT) {
            customer.deductPoints(POINTS_PER_DISCOUNT);
            return totalPrice * (1 - DISCOUNT_RATE); // Apply 10% discount
        }

        return totalPrice;
    }
}
