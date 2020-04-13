package com.gnopai.ji65.test;

import lombok.Value;

import java.util.List;

@Value
public class TestReport {
    List<TestResult> testResults;

    public boolean allTestsPassed() {
        return testResults.stream()
                .allMatch(TestResult::allAssertionsPassed);
    }

    public int getTotalTestsRun() {
        return testResults.size();
    }

    public long getTotalAssertionsPassed() {
        return testResults.stream()
                .mapToLong(TestResult::getTotalAssertionsPassed)
                .sum();
    }

    public long getTotalAssertionsFailed() {
        return testResults.stream()
                .mapToLong(TestResult::getTotalAssertionsFailed)
                .sum();
    }
}
