public class TargetProfitByDeliveryCost implements TargetProfitPolicy {
    @Override
    public void apply(MyFoodoraSystem system, double targetProfit) {
        double income = system.computeTotalIncome();
        int numOrders = system.getCompletedOrders().size();
        if (numOrders == 0) return;

        double avgPrice = income / numOrders;
        double markup = system.getMarkupPercentage();
        double serviceFee = system.getServiceFee();

        double deliveryCost = avgPrice * markup + serviceFee - (targetProfit / numOrders);
        system.setFees(serviceFee, markup, deliveryCost);
    }
}
