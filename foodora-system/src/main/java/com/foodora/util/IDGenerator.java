package com.foodora.util;

import java.util.HashMap;
import java.util.Map;

public class IDGenerator {
    private static final Map<String, Integer> counters = new HashMap<>();

    public static synchronized String generateID(String prefix) {
        int count = counters.getOrDefault(prefix, 0) + 1;
        counters.put(prefix, count);
        return String.format("%s-%04d", prefix, count);
    }
} 