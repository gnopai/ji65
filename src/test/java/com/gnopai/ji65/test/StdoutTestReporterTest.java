package com.gnopai.ji65.test;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StdoutTestReporterTest {

    @Test
    void testNoTests() {
        TestReport testReport = new TestReport(List.of());
        List<String> output = testCreateOutputShowingPassingTests(testReport);

        List<String> expectedOutput = List.of(
                "0 tests run: 0 assertions passed"
        );
        assertEquals(expectedOutput, output);
    }

    @Test
    void testSingleTestWithSinglePassingAssertion_dontShowPassing() {
        TestReport testReport = new TestReport(List.of(
                TestResult.builder()
                        .name("ima_test")
                        .assertionResult(new AssertionResult(true, (byte) 0x66, (byte) 0x66))
                        .build()
        ));
        List<String> output = testCreateOutputNotShowingPassingTests(testReport);

        List<String> expectedOutput = List.of(
                "1 test run: 1 assertion passed"
        );
        assertEquals(expectedOutput, output);
    }

    @Test
    void testSingleTestWithSinglePassingAssertion_showPassing() {
        TestReport testReport = new TestReport(List.of(
                TestResult.builder()
                        .name("ima_test")
                        .assertionResult(new AssertionResult(true, (byte) 0x66, (byte) 0x66))
                        .build()
        ));
        List<String> output = testCreateOutputShowingPassingTests(testReport);

        List<String> expectedOutput = List.of(
                "1 test run: 1 assertion passed",
                "    ima_test: assertion passed with value $66"
        );
        assertEquals(expectedOutput, output);
    }

    @Test
    void testSingleTestWithTwoPassingAssertions_showPassing() {
        TestReport testReport = new TestReport(List.of(
                TestResult.builder()
                        .name("ima_test")
                        .assertionResult(new AssertionResult(true, (byte) 0x01, (byte) 0x01))
                        .assertionResult(new AssertionResult(true, (byte) 0xFF, (byte) 0xFF))
                        .build()
        ));
        List<String> output = testCreateOutputShowingPassingTests(testReport);

        List<String> expectedOutput = List.of(
                "1 test run: 2 assertions passed",
                "    ima_test: assertion passed with value $01",
                "    ima_test: assertion passed with value $FF"
        );
        assertEquals(expectedOutput, output);
    }

    @Test
    void testSingleTestWithSingleFailingAssertion() {
        TestReport testReport = new TestReport(List.of(
                TestResult.builder()
                        .name("ima_test")
                        .assertionResult(new AssertionResult(false, (byte) 0x66, (byte) 0x00, "ohno"))
                        .build()
        ));
        List<String> output = testCreateOutputNotShowingPassingTests(testReport);

        List<String> expectedOutput = List.of(
                "1 test run: 0 assertions passed, 1 assertion FAILED",
                "    ima_test: assertion FAILED, expected $66 but got $00: ohno"
                );
        assertEquals(expectedOutput, output);
    }

    @Test
    void testSingleTestWithTwoFailingAssertions() {
        TestReport testReport = new TestReport(List.of(
                TestResult.builder()
                        .name("ima_test")
                        .assertionResult(new AssertionResult(false, (byte) 0x66, (byte) 0x00, "ohno"))
                        .assertionResult(new AssertionResult(false, (byte) 0x43, (byte) 0xFE))
                        .build()
        ));
        List<String> output = testCreateOutputNotShowingPassingTests(testReport);

        List<String> expectedOutput = List.of(
                "1 test run: 0 assertions passed, 2 assertions FAILED",
                "    ima_test: assertion FAILED, expected $66 but got $00: ohno",
                "    ima_test: assertion FAILED, expected $43 but got $FE"
        );
        assertEquals(expectedOutput, output);
    }

    @Test
    void testSingleTestWithTwoPassingAndTwoFailingAssertions_showPassing() {
        TestReport testReport = new TestReport(List.of(
                TestResult.builder()
                        .name("ima_test")
                        .assertionResult(new AssertionResult(false, (byte) 0x66, (byte) 0x00))
                        .assertionResult(new AssertionResult(true, (byte) 0x39, (byte) 0x39))
                        .assertionResult(new AssertionResult(false, (byte) 0x43, (byte) 0xFE, "hrm"))
                        .assertionResult(new AssertionResult(true, (byte) 0x0F, (byte) 0x0F))
                        .build()
        ));
        List<String> output = testCreateOutputShowingPassingTests(testReport);

        List<String> expectedOutput = List.of(
                "1 test run: 2 assertions passed, 2 assertions FAILED",
                "    ima_test: assertion FAILED, expected $66 but got $00",
                "    ima_test: assertion passed with value $39",
                "    ima_test: assertion FAILED, expected $43 but got $FE: hrm",
                "    ima_test: assertion passed with value $0F"
        );
        assertEquals(expectedOutput, output);
    }

    @Test
    void testSingleTestWithTwoPassingAndTwoFailingAssertions_dontShowPassing() {
        TestReport testReport = new TestReport(List.of(
                TestResult.builder()
                        .name("ima_test")
                        .assertionResult(new AssertionResult(false, (byte) 0x66, (byte) 0x00))
                        .assertionResult(new AssertionResult(true, (byte) 0x39, (byte) 0x39))
                        .assertionResult(new AssertionResult(false, (byte) 0x43, (byte) 0xFE, "eeee!"))
                        .assertionResult(new AssertionResult(true, (byte) 0x0F, (byte) 0x0F))
                        .build()
        ));
        List<String> output = testCreateOutputNotShowingPassingTests(testReport);

        List<String> expectedOutput = List.of(
                "1 test run: 2 assertions passed, 2 assertions FAILED",
                "    ima_test: assertion FAILED, expected $66 but got $00",
                "    ima_test: assertion FAILED, expected $43 but got $FE: eeee!"
        );
        assertEquals(expectedOutput, output);
    }

    @Test
    void testMultipleTestsAndAssertions_showPassing() {
        TestReport testReport = new TestReport(List.of(
                TestResult.builder()
                        .name("ima_test")
                        .assertionResult(new AssertionResult(false, (byte) 0x66, (byte) 0x00))
                        .assertionResult(new AssertionResult(true, (byte) 0x39, (byte) 0x39))
                        .build(),
                TestResult.builder()
                        .name("another_test")
                        .assertionResult(new AssertionResult(false, (byte) 0x43, (byte) 0xFE))
                        .assertionResult(new AssertionResult(true, (byte) 0x0F, (byte) 0x0F))
                        .build(),
                TestResult.builder()
                        .name("what")
                        .assertionResult(new AssertionResult(true, (byte) 0x20, (byte) 0x20))
                        .build()
        ));
        List<String> output = testCreateOutputShowingPassingTests(testReport);

        List<String> expectedOutput = List.of(
                "3 tests run: 3 assertions passed, 2 assertions FAILED",
                "    ima_test: assertion FAILED, expected $66 but got $00",
                "    ima_test: assertion passed with value $39",
                "    another_test: assertion FAILED, expected $43 but got $FE",
                "    another_test: assertion passed with value $0F",
                "    what: assertion passed with value $20"
        );
        assertEquals(expectedOutput, output);
    }

    @Test
    void testMultipleTestsAndAssertions_dontShowPassing() {
        TestReport testReport = new TestReport(List.of(
                TestResult.builder()
                        .name("ima_test")
                        .assertionResult(new AssertionResult(false, (byte) 0x66, (byte) 0x00))
                        .assertionResult(new AssertionResult(true, (byte) 0x39, (byte) 0x39))
                        .build(),
                TestResult.builder()
                        .name("another_test")
                        .assertionResult(new AssertionResult(false, (byte) 0x43, (byte) 0xFE, "derpy doo"))
                        .assertionResult(new AssertionResult(true, (byte) 0x0F, (byte) 0x0F))
                        .build(),
                TestResult.builder()
                        .name("what")
                        .assertionResult(new AssertionResult(true, (byte) 0x20, (byte) 0x20))
                        .build()
        ));
        List<String> output = testCreateOutputNotShowingPassingTests(testReport);

        List<String> expectedOutput = List.of(
                "3 tests run: 3 assertions passed, 2 assertions FAILED",
                "    ima_test: assertion FAILED, expected $66 but got $00",
                "    another_test: assertion FAILED, expected $43 but got $FE: derpy doo"
        );
        assertEquals(expectedOutput, output);
    }

    private List<String> testCreateOutputShowingPassingTests(TestReport testReport) {
        return StdoutTestReporter.builder()
                .showPassingTests(true)
                .build()
                .createOutput(testReport);
    }

    private List<String> testCreateOutputNotShowingPassingTests(TestReport testReport) {
        return StdoutTestReporter.builder()
                .showPassingTests(false)
                .build()
                .createOutput(testReport);
    }
}