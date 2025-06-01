import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class MostOrderedItemAlaCartePolicy implements OrderSortingPolicy {
    @Override
    public void sort(List<Order> completedOrders) {
        Map<String, Integer> countMap = new HashMap<>();

        for (Order order : completedOrders) {
            for (MenuItem item : order.getItems()) {
                String name = item.getName();
                countMap.put(name, countMap.getOrDefault(name, 0) + 1);
            }
        }

        countMap.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())  // Reverse order (descending) for most ordered
            .forEach(e -> System.out.println(e.getKey() + " -> " + e.getValue()));
    }
} 