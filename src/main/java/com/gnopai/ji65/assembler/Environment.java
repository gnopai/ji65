package com.gnopai.ji65.assembler;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@EqualsAndHashCode
@ToString
public class Environment<T> {
    private final Map<String, T> values = new HashMap<>();

    public Optional<T> get(String name) {
        return Optional.ofNullable(values.get(name));
    }

    public void define(String name, T value) {
        validateNotAlreadyDefined(name);
        values.put(name, value);
    }

    private void validateNotAlreadyDefined(String name) {
        if (values.containsKey(name)) {
            throw new RuntimeException("Duplicate declaration of \"" + name + "\"");
        }
    }
}
