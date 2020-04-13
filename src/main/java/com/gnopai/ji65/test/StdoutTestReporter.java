package com.gnopai.ji65.test;

import com.google.common.annotations.VisibleForTesting;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

public class StdoutTestReporter implements TestReporter {
    private final boolean showPassingTests;

    @Builder
    private StdoutTestReporter(boolean showPassingTests) {
        this.showPassingTests = showPassingTests;
    }

    @Override
    public void reportResults(TestReport testReport) {
        createOutput(testReport).forEach(System.out::println);
    }

    @VisibleForTesting
    List<String> createOutput(TestReport testReport) {
        List<String> output = new ArrayList<>();

        output.add(createSummaryOutput(testReport));

        testReport.getTestResults().stream()
                .flatMap(result -> createTestResultOutput(result).stream())
                .forEach(output::add);

        return output;
    }

    private String createSummaryOutput(TestReport testReport) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(format("%s run: %s passed",
                numberWithLabel(testReport.getTotalTestsRun(), "test"),
                numberWithLabel(testReport.getTotalAssertionsPassed(), "assertion")
        ));

        long totalAssertionsFailed = testReport.getTotalAssertionsFailed();
        if (totalAssertionsFailed > 0) {
            stringBuilder.append(format(", %s FAILED",
                    numberWithLabel(totalAssertionsFailed, "assertion")
            ));
        }

        return stringBuilder.toString();
    }

    private String numberWithLabel(long number, String label) {
        return format("%d %s", number, number == 1 ? label : label + "s");
    }

    private List<String> createTestResultOutput(TestResult testResult) {
        return testResult.getAssertionResults()
                .stream()
                .map(assertionResult -> createAssertionResultOutput(testResult, assertionResult))
                .flatMap(Optional::stream)
                .collect(toList());
    }

    private Optional<String> createAssertionResultOutput(TestResult testResult, AssertionResult assertionResult) {
        if (assertionResult.isFailure()) {
            return Optional.of(format("    %s: assertion FAILED, expected $%02X but got $%02X",
                    testResult.getName(),
                    assertionResult.getExpected(),
                    assertionResult.getActual()
            ));
        }

        if (showPassingTests) {
            return Optional.of(format("    %s: assertion passed with value $%02X",
                    testResult.getName(),
                    assertionResult.getActual()
            ));
        }

        return Optional.empty();
    }

    public static void main(String[] args) {
        TestReport testReport = new TestReport(List.of(
                TestResult.builder()
                        .name("testSomeStuff")
                        .assertionResult(new AssertionResult(true, (byte) 0x55, (byte) 0x55))
                        .assertionResult(new AssertionResult(true, (byte) 0x66, (byte) 0x66))
                        .build(),
                TestResult.builder()
                        .name("a_much_longer_test_name")
                        .assertionResult(new AssertionResult(true, (byte) 0x10, (byte) 0x10))
                        .assertionResult(new AssertionResult(false, (byte) 0xEF, (byte) 0xFF))
                        .build()
        ));
        new StdoutTestReporter(false)
                .reportResults(testReport);
    }
}
