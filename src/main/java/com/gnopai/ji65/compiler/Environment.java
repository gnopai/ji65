package com.gnopai.ji65.compiler;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Environment {
    private final Map<String, Integer> values = new HashMap<>();

    public Optional<Integer> get(String name) {
        return Optional.ofNullable(values.get(name));
    }

    public void define(String name, int value) {
        if (values.containsKey(name)) {
            throw new RuntimeException("Duplicate assignment of \"" + name + "\"");
        }
        values.put(name, value);
    }
}
