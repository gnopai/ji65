package com.gnopai.ji65.test;

import com.gnopai.ji65.Address;
import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.Program;
import com.gnopai.ji65.interpreter.Interpreter;
import com.gnopai.ji65.interpreter.ProgramEndStrategy;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TestRunnerTest {
    private final Interpreter interpreter = mock(Interpreter.class);
    private final TestResultTracker testResultTracker = mock(TestResultTracker.class);

    @org.junit.jupiter.api.Test
    void testRunStep_setValueOnRegister() {
        byte value = (byte) 0xFF;
        SetValue setValue = new SetValue(Target.X, null, value);
        Cpu cpu = Cpu.builder().build();

        TestRunner testClass = new TestRunner(interpreter, testResultTracker);

        testClass.runStep(cpu, setValue);

        assertEquals(value, cpu.getX());
    }

    @org.junit.jupiter.api.Test
    void testRunStep_setValueOnMemory() {
        byte value = (byte) 0xFF;
        Address address = new Address(0x1234);
        SetValue setValue = new SetValue(Target.MEMORY, address, value);
        Cpu cpu = Cpu.builder().build();

        TestRunner testClass = new TestRunner(interpreter, testResultTracker);

        testClass.runStep(cpu, setValue);

        assertEquals(value, cpu.getMemoryValue(address));
    }

    @org.junit.jupiter.api.Test
    void testRunStep_assertionOnMemory_passes() {
        byte value = (byte) 0xFF;
        Address address = new Address(0x1234);
        String message = "Foooooo";
        Assertion assertion = new Assertion(Target.MEMORY, address, value, message);
        Cpu cpu = Cpu.builder().build();
        cpu.setMemoryValue(address, value);

        TestRunner testClass = new TestRunner(interpreter, testResultTracker);

        testClass.runStep(cpu, assertion);

        verify(testResultTracker).assertionPassed(value);
        verifyNoMoreInteractions(testResultTracker);
    }

    @org.junit.jupiter.api.Test
    void testRunStep_assertionOnMemory_fails() {
        byte expectedValue = (byte) 0xFF;
        Address address = new Address(0x1234);
        String message = "Foooooo";
        Assertion assertion = new Assertion(Target.MEMORY, address, expectedValue, message);
        Cpu cpu = Cpu.builder().build();

        byte actualValue = (byte) 0x94;
        cpu.setMemoryValue(address, actualValue);

        TestRunner testClass = new TestRunner(interpreter, testResultTracker);

        testClass.runStep(cpu, assertion);

        verify(testResultTracker).assertionFailed(expectedValue, actualValue);
        verifyNoMoreInteractions(testResultTracker);
    }

    @org.junit.jupiter.api.Test
    void testRunStep_assertionOnRegister_passes() {
        byte value = (byte) 0xFF;
        String message = "Foooooo";
        Assertion assertion = new Assertion(Target.Y, null, value, message);
        Cpu cpu = Cpu.builder().build();
        cpu.setY(value);

        TestRunner testClass = new TestRunner(interpreter, testResultTracker);

        testClass.runStep(cpu, assertion);

        verify(testResultTracker).assertionPassed(value);
        verifyNoMoreInteractions(testResultTracker);
    }

    @org.junit.jupiter.api.Test
    void testRunStep_assertionOnRegister_fails() {
        byte expectedValue = (byte) 0xFF;
        String message = "Foooooo";
        Assertion assertion = new Assertion(Target.A, null, expectedValue, message);
        Cpu cpu = Cpu.builder().build();

        byte actualValue = (byte) 0x94;
        cpu.setAccumulator(actualValue);

        TestRunner testClass = new TestRunner(interpreter, testResultTracker);

        testClass.runStep(cpu, assertion);

        verify(testResultTracker).assertionFailed(expectedValue, actualValue);
        verifyNoMoreInteractions(testResultTracker);
    }

    @org.junit.jupiter.api.Test
    void testRunStep_runSubRoutine() {
        Address address = new Address(0x1234);
        RunSubRoutine runSubRoutine = new RunSubRoutine(address);
        Cpu cpu = Cpu.builder().build();

        TestRunner testClass = new TestRunner(interpreter, testResultTracker);

        testClass.runStep(cpu, runSubRoutine);

        verify(interpreter).run(eq(address), eq(cpu), any(ProgramEndStrategy.class));
        verifyNoMoreInteractions(testResultTracker);
    }

    @org.junit.jupiter.api.Test
    void testRunTests() {
        Address subRoutineAddress = new Address(0x7777);
        Program program = program(
                new Test("test 1", List.of(
                        new SetValue(Target.X, null, 0x55),
                        new Assertion(Target.X, null, 0x55, null)
                        )),
                new Test("test 2", List.of(
                        new RunSubRoutine(subRoutineAddress),
                        new Assertion(Target.X, null, 0x55, null)
                )),
                new Test("test 3", List.of(
                        new SetValue(Target.X, null, 0x55),
                        new SetValue(Target.Y, null, 0x66),
                        new Assertion(Target.X, null, 0x55, null),
                        new Assertion(Target.Y, null, 0x66, null)
                ))
        );

        List<TestResult> results = new TestRunner(interpreter, new TestResultTracker())
                .runTests(program);

        List<TestResult> expectedResults = List.of(
                new TestResult("test 1", List.of(
                        new AssertionResult(true, (byte) 0x55, (byte) 0x55)
                )),
                new TestResult("test 2", List.of(
                        new AssertionResult(false, (byte) 0x55, (byte) 0x0)
                )),
                new TestResult("test 3", List.of(
                        new AssertionResult(true, (byte) 0x55, (byte) 0x55),
                        new AssertionResult(true, (byte) 0x66, (byte) 0x66)
                ))
        );

        verify(interpreter).run(eq(subRoutineAddress), any(Cpu.class), any(ProgramEndStrategy.class));
        assertEquals(expectedResults, results);
    }

    private Program program(Test... tests) {
        return new Program(List.of(), Map.of("foo", 1234), List.of(tests));
    }

}