public class TargetProfitByServiceFee implements TargetProfitPolicy {
    @Override
    public void apply(MyFoodoraSystem system, double targetProfit) {
        double income = system.computeTotalIncome();
        int numOrders = system.getCompletedOrders().size();
        if (numOrders == 0) return;

        double avgPrice = income / numOrders;
        double markup = system.getMarkupPercentage();
        double deliveryCost = system.getDeliveryCost();

        double serviceFee = (targetProfit / numOrders) + deliveryCost - (avgPrice * markup);
        system.setFees(serviceFee, markup, deliveryCost);
    }
}
