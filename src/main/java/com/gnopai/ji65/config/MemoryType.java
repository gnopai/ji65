package com.gnopai.ji65.config;

import java.util.Optional;

public enum MemoryType {
    READ_ONLY("ro"),
    READ_WRITE("rw");

    private final String name;

    MemoryType(String name) {
        this.name = name;
    }

    public static Optional<MemoryType> fromName(String name) {
        for (MemoryType memoryType : values()) {
            if (memoryType.name.equalsIgnoreCase(name)) {
                return Optional.of(memoryType);
            }
        }
        return Optional.empty();
    }
}
