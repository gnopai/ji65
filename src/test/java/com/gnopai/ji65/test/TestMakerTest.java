package com.gnopai.ji65.test;

import com.gnopai.ji65.Address;
import com.gnopai.ji65.assembler.Environment;
import com.gnopai.ji65.assembler.TestData;
import com.gnopai.ji65.parser.expression.Expression;
import com.gnopai.ji65.parser.expression.ExpressionEvaluator;
import com.gnopai.ji65.parser.statement.TestStatement;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TestMakerTest {
    private final ExpressionEvaluator expressionEvaluator = mock(ExpressionEvaluator.class);
    private final Environment environment = mock(Environment.class);

    @Test
    void testSetValueStep_register() {
        Expression valueExpression = mock(Expression.class);
        when(expressionEvaluator.evaluate(valueExpression, environment))
                .thenReturn(77);
        TestStatement testStatement = TestStatement.builder()
                .type(TestStepType.SET)
                .target(Target.X)
                .value(valueExpression)
                .build();
        TestData testData = TestData.builder()
                .testName("whee")
                .statement(testStatement)
                .build();

        TestMaker testClass = new TestMaker(expressionEvaluator);

        com.gnopai.ji65.test.Test test = testClass.makeTest(testData, environment);

        com.gnopai.ji65.test.Test expectedTest = new com.gnopai.ji65.test.Test("whee", List.of(
                SetValue.builder()
                        .target(Target.X)
                        .value(77)
                        .build()
        ));
        assertEquals(expectedTest, test);
    }

    @Test
    void testSetValueStep_memory() {
        Expression addressExpression = mock(Expression.class);
        when(expressionEvaluator.evaluate(addressExpression, environment))
                .thenReturn(0x1234);
        Expression valueExpression = mock(Expression.class);
        when(expressionEvaluator.evaluate(valueExpression, environment))
                .thenReturn(77);
        TestStatement testStatement = TestStatement.builder()
                .type(TestStepType.SET)
                .target(Target.MEMORY)
                .targetAddress(addressExpression)
                .value(valueExpression)
                .build();
        TestData testData = TestData.builder()
                .testName("whee")
                .statement(testStatement)
                .build();

        TestMaker testClass = new TestMaker(expressionEvaluator);

        com.gnopai.ji65.test.Test test = testClass.makeTest(testData, environment);

        com.gnopai.ji65.test.Test expectedTest = new com.gnopai.ji65.test.Test("whee", List.of(
                SetValue.builder()
                        .target(Target.MEMORY)
                        .targetAddress(new Address(0x1234))
                        .value(77)
                        .build()
        ));
        assertEquals(expectedTest, test);
    }

    @Test
    void testSetValueStep_missingValue() {
        TestStatement testStatement = TestStatement.builder()
                .type(TestStepType.SET)
                .target(Target.X)
                .build();
        TestData testData = TestData.builder()
                .testName("whee")
                .statement(testStatement)
                .build();

        TestMaker testClass = new TestMaker(expressionEvaluator);

        assertThrows(RuntimeException.class, () -> testClass.makeTest(testData, environment));
    }

    @Test
    void testAssertionStep_register() {
        Expression valueExpression = mock(Expression.class);
        when(expressionEvaluator.evaluate(valueExpression, environment))
                .thenReturn(77);
        TestStatement testStatement = TestStatement.builder()
                .type(TestStepType.ASSERT)
                .target(Target.X)
                .value(valueExpression)
                .message("Woooo")
                .build();
        TestData testData = TestData.builder()
                .testName("whee")
                .statement(testStatement)
                .build();

        TestMaker testClass = new TestMaker(expressionEvaluator);

        com.gnopai.ji65.test.Test test = testClass.makeTest(testData, environment);

        com.gnopai.ji65.test.Test expectedTest = new com.gnopai.ji65.test.Test("whee", List.of(
                Assertion.builder()
                        .target(Target.X)
                        .expectedValue(77)
                        .message("Woooo")
                        .build()
        ));
        assertEquals(expectedTest, test);
    }

    @Test
    void testAssertionStep_memory() {
        Expression addressExpression = mock(Expression.class);
        when(expressionEvaluator.evaluate(addressExpression, environment))
                .thenReturn(0x1234);
        Expression valueExpression = mock(Expression.class);
        when(expressionEvaluator.evaluate(valueExpression, environment))
                .thenReturn(77);
        TestStatement testStatement = TestStatement.builder()
                .type(TestStepType.ASSERT)
                .target(Target.MEMORY)
                .targetAddress(addressExpression)
                .value(valueExpression)
                .message("Woooo")
                .build();
        TestData testData = TestData.builder()
                .testName("whee")
                .statement(testStatement)
                .build();

        TestMaker testClass = new TestMaker(expressionEvaluator);

        com.gnopai.ji65.test.Test test = testClass.makeTest(testData, environment);

        com.gnopai.ji65.test.Test expectedTest = new com.gnopai.ji65.test.Test("whee", List.of(
                Assertion.builder()
                        .target(Target.MEMORY)
                        .targetAddress(new Address(0x1234))
                        .expectedValue(77)
                        .message("Woooo")
                        .build()
        ));
        assertEquals(expectedTest, test);
    }

    @Test
    void testAssertionStep_missingValue() {
        TestStatement testStatement = TestStatement.builder()
                .type(TestStepType.ASSERT)
                .target(Target.X)
                .build();
        TestData testData = TestData.builder()
                .testName("whee")
                .statement(testStatement)
                .build();

        TestMaker testClass = new TestMaker(expressionEvaluator);

        assertThrows(RuntimeException.class, () -> testClass.makeTest(testData, environment));
    }

    @Test
    void testRunSubRoutineStep() {
        Expression targetAddress = mock(Expression.class);
        when(expressionEvaluator.evaluate(targetAddress, environment))
                .thenReturn(0x1234);
        TestStatement testStatement = TestStatement.builder()
                .type(TestStepType.RUN)
                .targetAddress(targetAddress)
                .build();
        TestData testData = TestData.builder()
                .testName("whee")
                .statement(testStatement)
                .build();

        TestMaker testClass = new TestMaker(expressionEvaluator);

        com.gnopai.ji65.test.Test test = testClass.makeTest(testData, environment);

        com.gnopai.ji65.test.Test expectedTest = new com.gnopai.ji65.test.Test("whee", List.of(
                RunSubRoutine.builder()
                        .address(new Address(0x1234))
                        .build()
        ));
        assertEquals(expectedTest, test);
    }

    @Test
    void testRunSubRoutineStep_missingAddress() {
        TestStatement testStatement = TestStatement.builder()
                .type(TestStepType.RUN)
                .build();
        TestData testData = TestData.builder()
                .testName("whee")
                .statement(testStatement)
                .build();

        TestMaker testClass = new TestMaker(expressionEvaluator);

        assertThrows(RuntimeException.class, () -> testClass.makeTest(testData, environment));
    }

    @Test
    void testMockMemoryStep() {
        Expression addressExpression = mock(Expression.class);
        when(expressionEvaluator.evaluate(addressExpression, environment))
                .thenReturn(0x1234);
        Expression valueExpression1 = mock(Expression.class);
        when(expressionEvaluator.evaluate(valueExpression1, environment))
                .thenReturn(0x4F);
        Expression valueExpression2 = mock(Expression.class);
        when(expressionEvaluator.evaluate(valueExpression2, environment))
                .thenReturn(0x09);

        TestStatement testStatement = TestStatement.builder()
                .type(TestStepType.MOCK)
                .targetAddress(addressExpression)
                .values(List.of(valueExpression1, valueExpression2))
                .build();
        TestData testData = TestData.builder()
                .testName("whee")
                .statement(testStatement)
                .build();

        TestMaker testClass = new TestMaker(expressionEvaluator);

        com.gnopai.ji65.test.Test test = testClass.makeTest(testData, environment);

        com.gnopai.ji65.test.Test expectedTest = new com.gnopai.ji65.test.Test("whee", List.of(
                MockMemory.builder()
                        .address(new Address(0x1234))
                        .values(List.of((byte) 0x4F, (byte) 0x09))
                        .build()
        ));
        assertEquals(expectedTest, test);
    }

    @Test
    void testVerifyReadStep() {
        Expression addressExpression = mock(Expression.class);
        when(expressionEvaluator.evaluate(addressExpression, environment))
                .thenReturn(0x1234);
        Expression countExpression = mock(Expression.class);
        when(expressionEvaluator.evaluate(countExpression, environment))
                .thenReturn(5);

        TestStatement testStatement = TestStatement.builder()
                .type(TestStepType.VERIFY_READ)
                .targetAddress(addressExpression)
                .value(countExpression)
                .build();
        TestData testData = TestData.builder()
                .testName("whee")
                .statement(testStatement)
                .build();

        TestMaker testClass = new TestMaker(expressionEvaluator);

        com.gnopai.ji65.test.Test test = testClass.makeTest(testData, environment);

        com.gnopai.ji65.test.Test expectedTest = new com.gnopai.ji65.test.Test("whee", List.of(
                VerifyRead.builder()
                        .address(new Address(0x1234))
                        .expectedCount(5)
                        .build()
        ));
        assertEquals(expectedTest, test);
    }

    @Test
    void testVerifyWriteStep() {
        Expression addressExpression = mock(Expression.class);
        when(expressionEvaluator.evaluate(addressExpression, environment))
                .thenReturn(0x1234);
        Expression valueExpression1 = mock(Expression.class);
        when(expressionEvaluator.evaluate(valueExpression1, environment))
                .thenReturn(0x4F);
        Expression valueExpression2 = mock(Expression.class);
        when(expressionEvaluator.evaluate(valueExpression2, environment))
                .thenReturn(0x09);

        TestStatement testStatement = TestStatement.builder()
                .type(TestStepType.VERIFY_WRITE)
                .targetAddress(addressExpression)
                .values(List.of(valueExpression1, valueExpression2))
                .build();
        TestData testData = TestData.builder()
                .testName("whee")
                .statement(testStatement)
                .build();

        TestMaker testClass = new TestMaker(expressionEvaluator);

        com.gnopai.ji65.test.Test test = testClass.makeTest(testData, environment);

        com.gnopai.ji65.test.Test expectedTest = new com.gnopai.ji65.test.Test("whee", List.of(
                VerifyWrite.builder()
                        .address(new Address(0x1234))
                        .expectedValues(List.of((byte) 0x4F, (byte) 0x09))
                        .build()
        ));
        assertEquals(expectedTest, test);
    }

    @Test
    void testMultipleSteps() {
        Expression setValueExpression = mock(Expression.class);
        when(expressionEvaluator.evaluate(setValueExpression, environment))
                .thenReturn(77);
        TestStatement setValueStatement = TestStatement.builder()
                .type(TestStepType.SET)
                .target(Target.X)
                .value(setValueExpression)
                .build();

        Expression runAddress = mock(Expression.class);
        when(expressionEvaluator.evaluate(runAddress, environment))
                .thenReturn(0x8056);
        TestStatement runStatement = TestStatement.builder()
                .type(TestStepType.RUN)
                .targetAddress(runAddress)
                .build();

        Expression assertValueExpression = mock(Expression.class);
        when(expressionEvaluator.evaluate(assertValueExpression, environment))
                .thenReturn(99);
        TestStatement assertStatement = TestStatement.builder()
                .type(TestStepType.ASSERT)
                .target(Target.X)
                .value(assertValueExpression)
                .build();

        TestData testData = TestData.builder()
                .testName("whee")
                .statement(setValueStatement)
                .statement(runStatement)
                .statement(assertStatement)
                .build();

        TestMaker testClass = new TestMaker(expressionEvaluator);

        com.gnopai.ji65.test.Test test = testClass.makeTest(testData, environment);

        com.gnopai.ji65.test.Test expectedTest = new com.gnopai.ji65.test.Test("whee", List.of(
                SetValue.builder()
                        .target(Target.X)
                        .value(77)
                        .build(),
                RunSubRoutine.builder()
                        .address(new Address(0x8056))
                        .build(),
                Assertion.builder()
                        .target(Target.X)
                        .expectedValue(99)
                        .build()
        ));
        assertEquals(expectedTest, test);
    }
}