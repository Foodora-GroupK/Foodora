import java.util.List;

public interface OrderSortingPolicy {
    void sort(List<Order> completedOrders);
}
