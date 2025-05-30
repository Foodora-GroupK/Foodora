import java.util.Random;

public class LotteryFidelityCard implements FidelityCard {
    private static final double WIN_PROBABILITY = 0.05; // 5% chance (just random number)

    @Override
    public double applyDiscount(double totalPrice, Customer customer) {
        Random random = new Random();
        if (random.nextDouble() < WIN_PROBABILITY) {
            System.out.println("🎉 Lucky day! You won a free meal!");
            return 0.0;
        }
        return totalPrice;
    }
}
