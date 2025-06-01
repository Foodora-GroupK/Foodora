package com.foodora.model;

import java.util.List;

public class Meal {
    public enum MealType {STANDARD, VEGETARIAN, GLUTEN_FREE}
    public enum MealSize {HALF_MEAL, FULL_MEAL}

    private String name;
    private List<MenuItem> items;
    private MealType mealType;
    private MealSize mealSize;
    private boolean is_meal_of_the_week;
    private double discountFactor;

    public Meal(String name, List<MenuItem> items, MealType mealType, MealSize mealSize, boolean is_meal_of_the_week, double discountFactor) {
        this.name = name;
        this.items = items;
        this.mealType = mealType;
        this.mealSize = mealSize;
        this.is_meal_of_the_week = is_meal_of_the_week;
        this.discountFactor = discountFactor;

        validateMeal();
    }

    private void validateMeal() {
        // validate item count based on meal size
        if (mealSize == MealSize.HALF_MEAL && items.size() != 2) {
            throw new IllegalArgumentException("Half-meal must have 2 items");
        } else if (mealSize == MealSize.FULL_MEAL && items.size() != 3) {
            throw new IllegalArgumentException("Full-meal must have 3 items.");
        }

        // validate meal type consistency
        for (MenuItem item : items) {
            if (mealType == MealType.VEGETARIAN && item.getType() != MenuItem.Type.VEGETARIAN) {
                throw new IllegalArgumentException("Vegetarian meal contains non-vegetarian item.");
            } else if (mealType == MealType.GLUTEN_FREE && !item.isGlutenFree()) {
                throw new IllegalArgumentException("Gluten-free meal contains item with gluten");
            }
        }

        // validate structure
        long num_starters = items.stream().filter(i -> i.getCategory() == MenuItem.Category.STARTER).count();
        long num_mains = items.stream().filter(i -> i.getCategory() == MenuItem.Category.MAIN_DISH).count();
        long num_desserts = items.stream().filter(i -> i.getCategory() == MenuItem.Category.DESSERT).count();

        if (mealSize == MealSize.HALF_MEAL) {
            if (!((num_starters == 1 && num_mains == 1) || (num_mains == 1 && num_desserts == 1))) {
                throw new IllegalArgumentException("Half-meal must be starter+main or main+dessert.");
            }
        } else if (mealSize == MealSize.FULL_MEAL) {
            if (!(num_starters == 1 && num_mains == 1 && num_desserts == 1)) {
                throw new IllegalArgumentException("Full-meal must have one starter, one main, one dessert.");
            }
        }
    }

    public boolean isHalfMeal() {
        return mealSize == MealSize.HALF_MEAL;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        double total = 0;
        for (MenuItem item : items) {
            total += item.getPrice();
        }
        return total * (1 - discountFactor);
    }
} 