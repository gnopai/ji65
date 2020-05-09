package com.gnopai.ji65.test;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestResultTrackerTest {

    @Test
    void testAssertionPassed() {
        TestResultTracker testClass = new TestResultTracker();
        testClass.startTest(test("one"));
        testClass.assertionPassed((byte) 0x12);
        List<TestResult> results = testClass.getResults();

        List<TestResult> expectedResults = List.of(
                TestResult.builder()
                        .name("one")
                        .assertionResult(new AssertionResult(true, (byte) 0x12, (byte) 0x12))
                        .build()
        );
        assertEquals(expectedResults, results);
    }

    @Test
    void testAssertionFailed() {
        TestResultTracker testClass = new TestResultTracker();
        testClass.startTest(test("one"));
        testClass.assertionFailed((byte) 0x04, (byte) 0x05, "ohno");
        List<TestResult> results = testClass.getResults();

        List<TestResult> expectedResults = List.of(
                TestResult.builder()
                        .name("one")
                        .assertionResult(new AssertionResult(false, (byte) 0x04, (byte) 0x05, "ohno"))
                        .build()
        );
        assertEquals(expectedResults, results);
    }

    @Test
    void testTestWithNoAssertions() {
        TestResultTracker testClass = new TestResultTracker();
        testClass.startTest(test("one"));
        List<TestResult> results = testClass.getResults();

        List<TestResult> expectedResults = List.of(
                TestResult.builder()
                        .name("one")
                        .build()
        );
        assertEquals(expectedResults, results);
    }

    @Test
    void testAssertionsWithNoTest() {
        TestResultTracker testClass = new TestResultTracker();
        String expectedMessage = "No current test to add assertion to";

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> testClass.assertionPassed((byte) 0x88));
        assertEquals(expectedMessage, exception.getMessage());

        exception = assertThrows(IllegalStateException.class, () -> testClass.assertionFailed((byte) 0x88, (byte) 0x89, "ohno"));
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void testMultipleTests() {
        TestResultTracker testClass = new TestResultTracker();
        testClass.startTest(test("one"));
        testClass.assertionPassed((byte) 0x12);
        testClass.assertionFailed((byte) 0x04, (byte) 0x05, "ohno");
        testClass.startTest(test("two"));
        testClass.assertionPassed((byte) 0x99);
        List<TestResult> results = testClass.getResults();

        List<TestResult> expectedResults = List.of(
                TestResult.builder()
                        .name("one")
                        .assertionResult(new AssertionResult(true, (byte) 0x12, (byte) 0x12))
                        .assertionResult(new AssertionResult(false, (byte) 0x04, (byte) 0x05, "ohno"))
                        .build(),
                TestResult.builder()
                        .name("two")
                        .assertionResult(new AssertionResult(true, (byte) 0x99, (byte) 0x99))
                        .build()
        );
        assertEquals(expectedResults, results);
    }

    @Test
    void testReset() {
        TestResultTracker testClass = new TestResultTracker();
        testClass.startTest(test("one"));
        testClass.assertionPassed((byte) 0x12);
        testClass.startTest(test("two"));
        testClass.assertionPassed((byte) 0x99);
        testClass.reset();
        List<TestResult> results = testClass.getResults();

        assertTrue(results.isEmpty());
    }

    private com.gnopai.ji65.test.Test test(String name) {
        return new com.gnopai.ji65.test.Test(name, List.of());
    }
}