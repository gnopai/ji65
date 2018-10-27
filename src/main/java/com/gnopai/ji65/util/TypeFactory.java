package com.gnopai.ji65.util;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toUnmodifiableMap;

public class TypeFactory<K, V> {
    private final Map<K, V> valuesByType;

    public TypeFactory(Function<V, K> typeFunction, List<V> values) {
        valuesByType = values.stream()
                .collect(toUnmodifiableMap(typeFunction, Function.identity()));
    }

    public V get(K type) {
        return valuesByType.get(type);
    }
}
