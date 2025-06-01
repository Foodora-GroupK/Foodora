import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class LeastOrderedHalfMealPolicy implements OrderSortingPolicy {
    @Override
    public void sort(List<Order> completedOrders) {
        Map<String, Integer> countMap = new HashMap<>();

        for (Order order : completedOrders) {
            for (Meal meal : order.getMeals()) {
                if (meal.isHalfMeal()) {
                    String name = meal.getName();
                    countMap.put(name, countMap.getOrDefault(name, 0) + 1);
                }
            }
        }

        countMap.entrySet().stream()
            .sorted(Map.Entry.comparingByValue())  // Natural order (ascending) for least ordered
            .forEach(e -> System.out.println(e.getKey() + " -> " + e.getValue()));
    }
} 