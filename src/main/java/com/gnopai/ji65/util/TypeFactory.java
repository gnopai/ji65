package com.gnopai.ji65.util;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class TypeFactory<K extends Enum<K>, V> {
    private final Map<K, V> valuesByType;

    public TypeFactory(Class<K> keyClass, Function<V, K> typeFunction, List<V> values) {
        valuesByType = new EnumMap<>(keyClass);
        values.forEach(value -> valuesByType.put(typeFunction.apply(value), value));
    }

    public V get(K type) {
        return valuesByType.get(type);
    }
}
