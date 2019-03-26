package com.gnopai.ji65.config;

import java.util.Optional;

public enum SegmentType {
    READ_ONLY("ro"),
    READ_WRITE("rw"),
    UNINITIALIZED("bss"),
    ZERO_PAGE("zp"),
    OVERWRITE("overwrite");

    private final String name;

    SegmentType(String name) {
        this.name = name;
    }

    public static Optional<SegmentType> fromName(String name) {
        for (SegmentType segmentType : values()) {
            if (segmentType.name.equalsIgnoreCase(name)) {
                return Optional.of(segmentType);
            }
        }
        return Optional.empty();
    }
}
