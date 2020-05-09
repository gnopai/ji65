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
        TestableCpu cpu = new TestableCpu();

        TestRunner testClass = new TestRunner(interpreter, testResultTracker);

        testClass.runStep(cpu, setValue);

        assertEquals(value, cpu.getX());
    }

    @org.junit.jupiter.api.Test
    void testRunStep_setValueOnMemory() {
        byte value = (byte) 0xFF;
        Address address = new Address(0x1234);
        SetValue setValue = new SetValue(Target.MEMORY, address, value);
        TestableCpu cpu = new TestableCpu();

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
        TestableCpu cpu = new TestableCpu();
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
        TestableCpu cpu = new TestableCpu();

        byte actualValue = (byte) 0x94;
        cpu.setMemoryValue(address, actualValue);

        TestRunner testClass = new TestRunner(interpreter, testResultTracker);

        testClass.runStep(cpu, assertion);

        verify(testResultTracker).assertionFailed(expectedValue, actualValue, message);
        verifyNoMoreInteractions(testResultTracker);
    }

    @org.junit.jupiter.api.Test
    void testRunStep_assertionOnRegister_passes() {
        byte value = (byte) 0xFF;
        String message = "Foooooo";
        Assertion assertion = new Assertion(Target.Y, null, value, message);
        TestableCpu cpu = new TestableCpu();
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
        TestableCpu cpu = new TestableCpu();

        byte actualValue = (byte) 0x94;
        cpu.setAccumulator(actualValue);

        TestRunner testClass = new TestRunner(interpreter, testResultTracker);

        testClass.runStep(cpu, assertion);

        verify(testResultTracker).assertionFailed(expectedValue, actualValue, message);
        verifyNoMoreInteractions(testResultTracker);
    }

    @org.junit.jupiter.api.Test
    void testRunStep_runSubRoutine() {
        Address address = new Address(0x1234);
        RunSubRoutine runSubRoutine = new RunSubRoutine(address);
        TestableCpu cpu = new TestableCpu();

        TestRunner testClass = new TestRunner(interpreter, testResultTracker);

        testClass.runStep(cpu, runSubRoutine);

        verify(interpreter).run(eq(address), eq(cpu), any(ProgramEndStrategy.class));
        verifyNoMoreInteractions(testResultTracker);
    }

    @org.junit.jupiter.api.Test
    void testRunStep_mockMemory() {
        List<Byte> values = List.of((byte) 0x55, (byte) 0xFE);
        Address address = new Address(0x1234);
        MockMemory mockMemory = new MockMemory(address, values);
        TestableCpu cpu = new TestableCpu();

        TestRunner testClass = new TestRunner(interpreter, testResultTracker);

        testClass.runStep(cpu, mockMemory);

        assertEquals(values, cpu.getMockedValues(address));
    }

    @org.junit.jupiter.api.Test
    void testRunStep_verifyRead_passes() {
        int expectedReads = 2;
        Address address = new Address(0x558E);
        VerifyRead verifyRead = new VerifyRead(address, expectedReads);
        TestableCpu cpu = new TestableCpu();

        cpu.addWatch(address);
        cpu.getMemoryValue(address);
        cpu.getMemoryValue(address);

        TestRunner testClass = new TestRunner(interpreter, testResultTracker);

        testClass.runStep(cpu, verifyRead);

        verify(testResultTracker).assertionPassed(expectedReads);
        verifyNoMoreInteractions(testResultTracker);
    }

    @org.junit.jupiter.api.Test
    void testRunStep_verifyRead_fails() {
        int expectedReads = 2;
        Address address = new Address(0x558E);
        VerifyRead verifyRead = new VerifyRead(address, expectedReads);
        TestableCpu cpu = new TestableCpu();

        cpu.addWatch(address);
        cpu.getMemoryValue(address);

        TestRunner testClass = new TestRunner(interpreter, testResultTracker);

        testClass.runStep(cpu, verifyRead);

        String expectedMessage = "Unexpected read count for address $558E";
        verify(testResultTracker).assertionFailed(expectedReads, 1, expectedMessage);
        verifyNoMoreInteractions(testResultTracker);
    }

    @org.junit.jupiter.api.Test
    void testRunStep_verifyWrite_passes() {
        List<Byte> expectedBytes = List.of((byte) 0x66, (byte) 0xEE, (byte) 0x80);
        Address address = new Address(0x558E);
        VerifyWrite verifyWrite = new VerifyWrite(address, expectedBytes);
        TestableCpu cpu = new TestableCpu();

        cpu.addWatch(address);
        cpu.setMemoryValue(address, expectedBytes.get(0));
        cpu.setMemoryValue(address, expectedBytes.get(1));
        cpu.setMemoryValue(address, expectedBytes.get(2));

        TestRunner testClass = new TestRunner(interpreter, testResultTracker);

        testClass.runStep(cpu, verifyWrite);

        verify(testResultTracker).assertionPassed(expectedBytes);
        verifyNoMoreInteractions(testResultTracker);
    }

    @org.junit.jupiter.api.Test
    void testRunStep_verifyWrite_fails() {
        List<Byte> expectedBytes = List.of((byte) 0x66, (byte) 0xEE, (byte) 0x80);
        Address address = new Address(0x558E);
        VerifyWrite verifyWrite = new VerifyWrite(address, expectedBytes);
        TestableCpu cpu = new TestableCpu();

        cpu.addWatch(address);
        cpu.setMemoryValue(address, (byte) 0x99);
        cpu.setMemoryValue(address, (byte) 0x66);

        TestRunner testClass = new TestRunner(interpreter, testResultTracker);

        testClass.runStep(cpu, verifyWrite);

        List<Byte> actualBytes = List.of((byte) 0x99, (byte) 0x66);
        String expectedMessage = "Unexpected bytes written to address $558E";
        verify(testResultTracker).assertionFailed(expectedBytes, actualBytes, expectedMessage);
        verifyNoMoreInteractions(testResultTracker);
    }

    @org.junit.jupiter.api.Test
    void testRunTests() {
        Address address1 = new Address(0x549A);
        Address address2 = new Address(0x7DE8);
        byte byteToWrite = (byte) 0xA5;
        Address subRoutineAddress = new Address(0x7777);
        Program program = program(
                new Test("test 1", List.of(
                        new SetValue(Target.X, null, 0x55),
                        new VerifyRead(address1, 9),
                        new Assertion(Target.X, null, 0x55, null)
                        )),
                new Test("test 2", List.of(
                        new RunSubRoutine(subRoutineAddress),
                        new Assertion(Target.X, null, 0x55, null)
                )),
                new Test("test 3", List.of(
                        (runner, cpu) -> {
                            cpu.getMemoryValue(address1);
                            cpu.getMemoryValue(address1);
                            cpu.setMemoryValue(address2, byteToWrite);
                        },
                        new SetValue(Target.X, null, 0x55),
                        new SetValue(Target.Y, null, 0x66),
                        new VerifyRead(address1, 2),
                        new VerifyWrite(address2, List.of(byteToWrite)),
                        new Assertion(Target.X, null, 0x55, null),
                        new Assertion(Target.Y, null, 0x66, null)
                ))
        );

        List<TestResult> results = new TestRunner(interpreter, new TestResultTracker())
                .runTests(program);

        List<TestResult> expectedResults = List.of(
                new TestResult("test 1", List.of(
                        new AssertionResult(false, 9, 0, "Unexpected read count for address $549A"),
                        new AssertionResult(true, (byte) 0x55, (byte) 0x55)
                )),
                new TestResult("test 2", List.of(
                        new AssertionResult(false, (byte) 0x55, (byte) 0x0)
                )),
                new TestResult("test 3", List.of(
                        new AssertionResult(true, 2, 2),
                        new AssertionResult(true, List.of(byteToWrite), List.of(byteToWrite)),
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