package com.gnopai.ji65.test;

import java.util.List;

public class StdoutTestReporter implements TestReporter {

    @Override
    public void reportResults(List<TestResult> testResults) {
        testResults.forEach(this::reportTestResult);
    }

    private void reportTestResult(TestResult testResult) {
        System.out.printf("Test '%s': ", testResult.getName());
        testResult.getAssertionResults().forEach(this::reportAssertionResult);
    }

    private void reportAssertionResult(AssertionResult assertionResult) {
        if (assertionResult.isSuccessful()) {
            System.out.printf("\tAssertion passed with value '%s'\n", assertionResult.getActual());
        } else {
            System.out.printf("\tAssertion FAILED, expected '%s' but got '%s'\n",
                    assertionResult.getExpected(),
                    assertionResult.getActual());
        }
    }
}
