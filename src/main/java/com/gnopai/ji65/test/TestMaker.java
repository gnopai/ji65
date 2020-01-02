package com.gnopai.ji65.test;

import com.gnopai.ji65.Address;
import com.gnopai.ji65.assembler.Environment;
import com.gnopai.ji65.assembler.TestData;
import com.gnopai.ji65.parser.expression.Expression;
import com.gnopai.ji65.parser.expression.ExpressionEvaluator;
import com.gnopai.ji65.parser.statement.TestStatement;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TestMaker {
    private final ExpressionEvaluator expressionEvaluator;

    @Inject
    public TestMaker(ExpressionEvaluator expressionEvaluator) {
        this.expressionEvaluator = expressionEvaluator;
    }

    public Test makeTest(TestData testData, Environment environment) {
        List<TestStep> testSteps = testData.getStatements()
                .stream()
                .map(testStatement -> makeStep(testStatement, environment))
                .collect(Collectors.toList());
        return new Test(testData.getTestName(), testSteps);
    }

    private TestStep makeStep(TestStatement testStatement, Environment environment) {
        switch (testStatement.getType()) {
            case SET:
                return makeSetValueStep(testStatement, environment);
            case RUN:
                return makeRunSubRoutineStep(testStatement, environment);
            case ASSERT:
                return makeAssertionStep(testStatement, environment);
        }

        throw new IllegalStateException("Unknown test type: " + testStatement.getType());
    }

    private TestStep makeSetValueStep(TestStatement testStatement, Environment environment) {
        return SetValue.builder()
                .target(testStatement.getTarget())
                .targetAddress(resolveAddress(testStatement.getTargetAddress(), environment)
                        .orElse(null))
                .value(resolveExpression(testStatement.getValue(), environment)
                        .orElseThrow())
                .build();
    }

    private TestStep makeAssertionStep(TestStatement testStatement, Environment environment) {
        return Assertion.builder()
                .target(testStatement.getTarget())
                .targetAddress(resolveAddress(testStatement.getTargetAddress(), environment)
                        .orElse(null))
                .expectedValue(resolveExpression(testStatement.getValue(), environment)
                        .orElseThrow())
                .message(testStatement.getMessage())
                .build();
    }

    private TestStep makeRunSubRoutineStep(TestStatement testStatement, Environment environment) {
        return RunSubRoutine.builder()
                .address(resolveAddress(testStatement.getTargetAddress(), environment)
                        .orElseThrow())
                .build();
    }

    private Optional<Integer> resolveExpression(Expression expression, Environment environment) {
        return Optional.ofNullable(expression)
                .map(e -> expressionEvaluator.evaluate(e, environment));
    }

    private Optional<Address> resolveAddress(Expression expression, Environment environment) {
        return resolveExpression(expression, environment)
                .map(Address::new);
    }
}
