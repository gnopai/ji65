package com.gnopai.ji65.test;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class TestResult {
    String name;
    @Singular
    List<AssertionResult> assertionResults;

    public boolean allAssertionsPassed() {
        return assertionResults.stream()
                .allMatch(AssertionResult::isSuccessful);
    }

    public long getTotalAssertionsPassed() {
        return assertionResults.stream()
                .filter(AssertionResult::isSuccessful)
                .count();
    }

    public long getTotalAssertionsFailed() {
        return assertionResults.stream()
                .filter(AssertionResult::isFailure)
                .count();
    }
}
