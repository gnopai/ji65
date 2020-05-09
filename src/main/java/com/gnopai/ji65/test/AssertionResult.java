package com.gnopai.ji65.test;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class AssertionResult {
    boolean successful;
    Object expected;
    Object actual;
    String message;

    public AssertionResult(boolean successful, Object expected, Object actual) {
        this(successful, expected, actual, null);
    }

    public boolean isFailure() {
        return !successful;
    }

    public String getActualAsString() {
        return asString(actual);
    }

    public String getExpectedAsString() {
        return asString(expected);
    }

    private String asString(Object object) {
        return object instanceof Byte ? String.format("$%02X", object) : object.toString();
    }
}
