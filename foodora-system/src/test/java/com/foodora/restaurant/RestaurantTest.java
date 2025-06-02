package com.foodora.restaurant;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import com.foodora.CLUI.Restaurant;
import com.foodora.CLUI.Dish;
import com.foodora.CLUI.Meal;

public class RestaurantTest {
    private Restaurant restaurant;
    private Dish testDish;
    private Meal testMeal;

    @BeforeEach
    void setUp() {
        restaurant = new Restaurant("Test Restaurant", "10.0,20.0");
        testDish = new Dish("Test Dish", "main", "standard", 15.99);
        testMeal = new Meal("Test Meal");
    }

    @Test
    void testRestaurantCreation() {
        assertEquals("Test Restaurant", restaurant.name);
        assertEquals("10.0,20.0", restaurant.location);
        assertNotNull(restaurant.menu);
        assertNotNull(restaurant.meals);
        assertTrue(restaurant.menu.isEmpty());
        assertTrue(restaurant.meals.isEmpty());
    }

    @Test
    void testAddDishToMenu() {
        restaurant.menu.put(testDish.name, testDish);
        assertTrue(restaurant.menu.containsKey("Test Dish"));
        assertEquals(testDish, restaurant.menu.get("Test Dish"));
    }

    @Test
    void testCreateMeal() {
        restaurant.meals.put(testMeal.name, testMeal);
        assertTrue(restaurant.meals.containsKey("Test Meal"));
        assertEquals(testMeal, restaurant.meals.get("Test Meal"));
    }

    @Test
    void testAddDishToMeal() {
        restaurant.menu.put(testDish.name, testDish);
        restaurant.meals.put(testMeal.name, testMeal);
        
        testMeal.dishes.add(testDish);
        
        assertEquals(1, testMeal.dishes.size());
        assertEquals(testDish, testMeal.dishes.get(0));
    }

    @Test
    void testSpecialOffer() {
        restaurant.meals.put(testMeal.name, testMeal);
        assertFalse(testMeal.isSpecialOffer);
        
        testMeal.isSpecialOffer = true;
        assertTrue(testMeal.isSpecialOffer);
    }

    @Test
    void testDishValidation() {
        // Test valid dish categories
        assertDoesNotThrow(() -> new Dish("Soup", "starter", "standard", 10.0));
        assertDoesNotThrow(() -> new Dish("Steak", "main", "standard", 25.0));
        assertDoesNotThrow(() -> new Dish("Cake", "dessert", "standard", 8.0));

        // Test valid food types
        assertDoesNotThrow(() -> new Dish("Dish1", "main", "standard", 10.0));
        assertDoesNotThrow(() -> new Dish("Dish2", "main", "vegetarian", 10.0));
        assertDoesNotThrow(() -> new Dish("Dish3", "main", "gluten-free", 10.0));

        // Test price validation
        assertThrows(IllegalArgumentException.class, () -> 
            new Dish("Invalid", "main", "standard", -1.0));
    }

    @Test
    void testMealPriceCalculation() {
        Dish dish1 = new Dish("Dish1", "starter", "standard", 10.0);
        Dish dish2 = new Dish("Dish2", "main", "standard", 20.0);
        
        testMeal.dishes.add(dish1);
        testMeal.dishes.add(dish2);
        
        double expectedTotal = 30.0; // Sum of dish prices
        double actualTotal = testMeal.dishes.stream()
                                          .mapToDouble(d -> d.price)
                                          .sum();
        
        assertEquals(expectedTotal, actualTotal);
    }
} 