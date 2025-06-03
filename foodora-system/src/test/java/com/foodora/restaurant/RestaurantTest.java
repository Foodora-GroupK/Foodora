package com.foodora.restaurant;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import com.foodora.user.Restaurant;
import com.foodora.model.MenuItem;
import com.foodora.model.Meal;
import com.foodora.util.Coordinate;
import java.util.Arrays;

public class RestaurantTest {
    private Restaurant restaurant;
    private MenuItem testItem;
    private Meal testMeal;

    @BeforeEach
    void setUp() {
        restaurant = new Restaurant(
            "Test Restaurant",
            new Coordinate(10.0, 20.0),
            "testrestaurant",
            "password"
        );
        
        testItem = new MenuItem(
            "Test Item",
            15.99,
            MenuItem.Category.MAIN_DISH,
            MenuItem.Type.STANDARD,
            false
        );
    }

    @Test
    void testRestaurantCreation() {
        assertEquals("Test Restaurant", restaurant.getName());
        assertEquals(10.0, restaurant.getLocation().getX());
        assertEquals(20.0, restaurant.getLocation().getY());
        assertNotNull(restaurant.getMenu());
        assertNotNull(restaurant.getMeals());
        assertTrue(restaurant.getMenu().getItems().isEmpty());
        assertTrue(restaurant.getMeals().isEmpty());
    }

    @Test
    void testAddItemToMenu() {
        restaurant.addMenuItem(testItem);
        assertTrue(restaurant.getMenu().getItems().contains(testItem));
    }

    @Test
    void testCreateMeal() {
        restaurant.addMenuItem(testItem);
        restaurant.createMeal(
            "Test Meal",
            Arrays.asList(testItem),
            Meal.MealType.STANDARD,
            Meal.MealSize.MEDIUM,
            false
        );
        
        assertEquals(1, restaurant.getMeals().size());
        Meal meal = restaurant.getMeals().get(0);
        assertEquals("Test Meal", meal.getName());
        assertTrue(meal.getItems().contains(testItem));
    }

    @Test
    void testMealOfTheWeek() {
        restaurant.addMenuItem(testItem);
        restaurant.createMeal(
            "Special Meal",
            Arrays.asList(testItem),
            Meal.MealType.STANDARD,
            Meal.MealSize.MEDIUM,
            true // is meal of the week
        );
        
        assertEquals(1, restaurant.getMeals().size());
        Meal meal = restaurant.getMeals().get(0);
        assertTrue(meal.isMealOfTheWeek());
    }

    @Test
    void testMenuItemValidation() {
        // Test valid menu items
        assertDoesNotThrow(() -> new MenuItem(
            "Soup",
            10.0,
            MenuItem.Category.STARTER,
            MenuItem.Type.STANDARD,
            false
        ));
        
        assertDoesNotThrow(() -> new MenuItem(
            "Steak",
            25.0,
            MenuItem.Category.MAIN_DISH,
            MenuItem.Type.STANDARD,
            false
        ));
        
        assertDoesNotThrow(() -> new MenuItem(
            "Cake",
            8.0,
            MenuItem.Category.DESSERT,
            MenuItem.Type.STANDARD,
            false
        ));

        // Test valid food types
        assertDoesNotThrow(() -> new MenuItem(
            "Dish1",
            10.0,
            MenuItem.Category.MAIN_DISH,
            MenuItem.Type.STANDARD,
            false
        ));
        
        assertDoesNotThrow(() -> new MenuItem(
            "Dish2",
            10.0,
            MenuItem.Category.MAIN_DISH,
            MenuItem.Type.VEGETARIAN,
            false
        ));
        
        assertDoesNotThrow(() -> new MenuItem(
            "Dish3",
            10.0,
            MenuItem.Category.MAIN_DISH,
            MenuItem.Type.STANDARD,
            true // gluten-free
        ));

        // Test price validation
        assertThrows(IllegalArgumentException.class, () -> 
            new MenuItem("Invalid", -1.0, MenuItem.Category.MAIN_DISH, MenuItem.Type.STANDARD, false));
    }

    @Test
    void testMealPriceCalculation() {
        MenuItem item1 = new MenuItem(
            "Item1",
            10.0,
            MenuItem.Category.STARTER,
            MenuItem.Type.STANDARD,
            false
        );
        
        MenuItem item2 = new MenuItem(
            "Item2",
            20.0,
            MenuItem.Category.MAIN_DISH,
            MenuItem.Type.STANDARD,
            false
        );
        
        restaurant.addMenuItem(item1);
        restaurant.addMenuItem(item2);
        
        restaurant.createMeal(
            "Test Meal",
            Arrays.asList(item1, item2),
            Meal.MealType.STANDARD,
            Meal.MealSize.MEDIUM,
            false
        );
        
        Meal meal = restaurant.getMeals().get(0);
        double expectedTotal = 30.0 * (1 - restaurant.getDefaultGenericDiscountFactor()); // Sum of item prices with default discount
        assertEquals(expectedTotal, meal.getPrice(), 0.01);
    }
} 