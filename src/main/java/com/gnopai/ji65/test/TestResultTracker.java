package com.gnopai.ji65.test;

import java.util.ArrayList;
import java.util.List;

public class TestResultTracker {
    private List<TestResult> testResults;
    private TestResult.TestResultBuilder currentTest;

    public TestResultTracker() {
        reset();
    }

    public void reset() {
        testResults = new ArrayList<>();
        currentTest = null;
    }

    public void startTest(Test test) {
        endCurrentTest();
        currentTest = TestResult.builder()
                .name(test.getName());
    }

    public void assertionPassed(byte value) {
        ensureTestStarted();
        AssertionResult assertionResult = new AssertionResult(true, value, value);
        currentTest.assertionResult(assertionResult);
    }

    public void assertionFailed(byte expected, byte actual) {
        ensureTestStarted();
        AssertionResult assertionResult = new AssertionResult(false, expected, actual);
        currentTest.assertionResult(assertionResult);
    }

    private void ensureTestStarted() {
        if (currentTest == null) {
            throw new IllegalStateException("No current test to add assertion to");
        }
    }

    public List<TestResult> getResults() {
        endCurrentTest();
        return testResults;
    }

    private void endCurrentTest() {
        if (currentTest == null) {
            return;
        }

        testResults.add(currentTest.build());
        currentTest = null;
    }

}
