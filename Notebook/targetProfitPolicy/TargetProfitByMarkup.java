public class TargetProfitByMarkup implements TargetProfitPolicy {
    @Override
    public void apply(MyFoodoraSystem system, double targetProfit) {
        double income = system.computeTotalIncome();
        int numOrders = system.getCompletedOrders().size();
        if (numOrders == 0) return;

        double avgPrice = income / numOrders;
        double serviceFee = system.getServiceFee();
        double deliveryCost = system.getDeliveryCost();

        double markup = ((targetProfit / numOrders) + deliveryCost - serviceFee) / avgPrice;
        system.setFees(serviceFee, markup, deliveryCost);
    }
}
