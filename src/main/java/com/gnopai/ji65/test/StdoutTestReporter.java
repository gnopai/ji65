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
            return Optional.of(format("    %s: assertion FAILED, expected %s but got %s%s",
                    testResult.getName(),
                    assertionResult.getExpectedAsString(),
                    assertionResult.getActualAsString(),
                    Optional.ofNullable(assertionResult.getMessage()).map(m -> ": " + m).orElse("")
            ));
        }

        if (showPassingTests) {
            return Optional.of(format("    %s: assertion passed with value %s",
                    testResult.getName(),
                    assertionResult.getActualAsString()
            ));
        }

        return Optional.empty();
    }
}
