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

import static java.util.stream.Collectors.toList;

public class TestMaker {
    private final ExpressionEvaluator expressionEvaluator;

    @Inject
    public TestMaker(ExpressionEvaluator expressionEvaluator) {
        this.expressionEvaluator = expressionEvaluator;
    }

    public Test makeTest(TestData testData, Environment environment) {
        List<TestStep> testSteps = testData.getStatements()
                .stream()
                .map(testStatement -> testStatement.makeTestStep(this, environment))
                .collect(toList());
        return new Test(testData.getTestName(), testSteps);
    }

    TestStep makeSetValueStep(TestStatement testStatement, Environment environment) {
        return SetValue.builder()
                .target(testStatement.getTarget())
                .targetAddress(resolveAddress(testStatement.getTargetAddress(), environment)
                        .orElse(null))
                .value(resolveExpression(testStatement.getFirstValue(), environment)
                        .orElseThrow())
                .build();
    }

    TestStep makeAssertionStep(TestStatement testStatement, Environment environment) {
        return Assertion.builder()
                .target(testStatement.getTarget())
                .targetAddress(resolveAddress(testStatement.getTargetAddress(), environment)
                        .orElse(null))
                .expectedValue(resolveExpression(testStatement.getFirstValue(), environment)
                        .orElseThrow())
                .message(testStatement.getMessage())
                .build();
    }

    TestStep makeRunSubRoutineStep(TestStatement testStatement, Environment environment) {
        return RunSubRoutine.builder()
                .address(resolveAddress(testStatement.getTargetAddress(), environment)
                        .orElseThrow())
                .build();
    }

    TestStep makeMockMemoryStep(TestStatement testStatement, Environment environment) {
        return MockMemory.builder()
                .address(resolveAddress(testStatement.getTargetAddress(), environment)
                        .orElseThrow())
                .values(resolveExpressions(testStatement.getValues(), environment))
                .build();
    }

    TestStep makeVerifyReadStep(TestStatement testStatement, Environment environment) {
        return VerifyRead.builder()
                .address(resolveAddress(testStatement.getTargetAddress(), environment)
                        .orElseThrow())
                .expectedCount(resolveExpression(testStatement.getFirstValue(), environment)
                        .orElseThrow())
                .build();
    }

    TestStep makeVerifyWriteStep(TestStatement testStatement, Environment environment) {
        return VerifyWrite.builder()
                .address(resolveAddress(testStatement.getTargetAddress(), environment)
                        .orElseThrow())
                .expectedValues(resolveExpressions(testStatement.getValues(), environment))
                .build();
    }

    private List<Byte> resolveExpressions(List<Expression> expressions, Environment environment) {
        return expressions.stream()
                .map(expression -> resolveExpression(expression, environment))
                .map(Optional::orElseThrow)
                .map(Integer::byteValue)
                .collect(toList());
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
