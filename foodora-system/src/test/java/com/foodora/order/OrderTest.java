package com.foodora.order;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import com.foodora.CLUI.Order;
import com.foodora.CLUI.Dish;
import java.time.LocalDate;

public class OrderTest {
    private Order order;
    private Dish testDish1;
    private Dish testDish2;

    @BeforeEach
    void setUp() {
        order = new Order("TestRestaurant", "ORDER001");
        testDish1 = new Dish("Dish1", "main", "standard", 15.99);
        testDish2 = new Dish("Dish2", "dessert", "vegetarian", 8.99);
    }

    @Test
    void testOrderCreation() {
        assertEquals("TestRestaurant", order.restaurantName);
        assertEquals("ORDER001", order.orderId);
        assertNotNull(order.items);
        assertTrue(order.items.isEmpty());
        assertNull(order.date);
    }

    @Test
    void testAddItems() {
        order.items.add(testDish1);
        assertEquals(1, order.items.size());
        assertEquals(testDish1, order.items.get(0));

        order.items.add(testDish2);
        assertEquals(2, order.items.size());
        assertEquals(testDish2, order.items.get(1));
    }

    @Test
    void testOrderTotal() {
        order.items.add(testDish1);
        order.items.add(testDish2);
        
        double expectedTotal = testDish1.price + testDish2.price;
        double actualTotal = order.items.stream()
                                      .mapToDouble(d -> d.price)
                                      .sum();
        
        assertEquals(expectedTotal, actualTotal);
    }

    @Test
    void testSetOrderDate() {
        String testDate = "2024-03-20";
        order.date = testDate;
        assertEquals(testDate, order.date);
    }

    @Test
    void testDateParsing() {
        String validDate = "2024-03-20";
        assertDoesNotThrow(() -> LocalDate.parse(validDate));

        String invalidDate = "2024-13-45";
        assertThrows(Exception.class, () -> LocalDate.parse(invalidDate));
    }

    @Test
    void testEmptyOrder() {
        assertTrue(order.items.isEmpty());
        assertEquals(0.0, order.items.stream()
                                   .mapToDouble(d -> d.price)
                                   .sum());
    }

    @Test
    void testMultipleSameItems() {
        // Adding same dish multiple times
        order.items.add(testDish1);
        order.items.add(testDish1);
        order.items.add(testDish1);
        
        assertEquals(3, order.items.size());
        assertEquals(testDish1.price * 3, order.items.stream()
                                                    .mapToDouble(d -> d.price)
                                                    .sum());
    }

    @Test
    void testMixedOrder() {
        // Testing mix of standard, vegetarian, and special items
        Dish standardDish = new Dish("Standard", "main", "standard", 10.0);
        Dish vegetarianDish = new Dish("Veggie", "main", "vegetarian", 12.0);
        Dish glutenFreeDish = new Dish("GF", "main", "gluten-free", 15.0);
        
        order.items.add(standardDish);
        order.items.add(vegetarianDish);
        order.items.add(glutenFreeDish);
        
        assertEquals(3, order.items.size());
        assertEquals(37.0, order.items.stream()
                                    .mapToDouble(d -> d.price)
                                    .sum());
    }
} 