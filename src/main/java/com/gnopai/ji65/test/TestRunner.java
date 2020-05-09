package com.gnopai.ji65.test;

import com.gnopai.ji65.Address;
import com.gnopai.ji65.Program;
import com.gnopai.ji65.interpreter.EndProgramAtRtsOnEmptyStack;
import com.gnopai.ji65.interpreter.Interpreter;

import javax.inject.Inject;
import java.util.List;

public class TestRunner {
    private final Interpreter interpreter;
    private final TestResultTracker testResultTracker;

    @Inject
    public TestRunner(Interpreter interpreter, TestResultTracker testResultTracker) {
        this.interpreter = interpreter;
        this.testResultTracker = testResultTracker;
    }

    public List<TestResult> runTests(Program program) {
        testResultTracker.reset();
        program.getTests().forEach(test -> runTest(test, program));
        return testResultTracker.getResults();
    }

    private void runTest(Test test, Program program) {
        TestableCpu cpu = new TestableCpu();
        test.getSteps().stream()
                .map(TestStep::getWatchedAddresses)
                .flatMap(List::stream)
                .distinct()
                .forEach(cpu::addWatch);

        cpu.load(program);
        testResultTracker.startTest(test);
        test.getSteps()
                .forEach(step -> step.run(this, cpu));
    }

    void runStep(TestableCpu cpu, SetValue setValue) {
        Target target = setValue.getTarget();
        byte value = (byte) setValue.getValue();

        if (target.equals(Target.MEMORY)) {
            cpu.setMemoryValue(setValue.getTargetAddress(), value);
        } else {
            target.setValue(cpu, value);
        }
    }

    void runStep(TestableCpu cpu, Assertion assertion) {
        byte value = getValue(cpu, assertion.getTarget(), assertion.getTargetAddress());
        byte expectedValue = (byte) assertion.getExpectedValue();
        doAssert(expectedValue, value, assertion.getMessage());
    }

    void runStep(TestableCpu cpu, RunSubRoutine runSubRoutine) {
        interpreter.run(runSubRoutine.getAddress(), cpu, new EndProgramAtRtsOnEmptyStack());
    }

    void runStep(TestableCpu cpu, MockMemory mockMemory) {
        cpu.mockMemoryValues(mockMemory.getAddress(), mockMemory.getValues());
    }

    void runStep(TestableCpu cpu, VerifyRead verifyRead) {
        int actualReadCount = cpu.getWatchReadCount(verifyRead.getAddress());
        String message = "Unexpected read count for address " + verifyRead.getAddress();
        doAssert(verifyRead.getExpectedCount(), actualReadCount, message);
    }

    void runStep(TestableCpu cpu, VerifyWrite verifyWrite) {
        List<Byte> actualBytesWritten = cpu.getWatchBytesWritten(verifyWrite.getAddress());
        String message = "Unexpected bytes written to address " + verifyWrite.getAddress();
        doAssert(verifyWrite.getExpectedValues(), actualBytesWritten, message);
    }

    private <T> void doAssert(T expected, T actual, String message) {
        if (actual.equals(expected)) {
            testResultTracker.assertionPassed(actual);
        } else {
            testResultTracker.assertionFailed(expected, actual, message);
        }
    }

    private Byte getValue(TestableCpu cpu, Target target, Address targetAddress) {
        if (target.equals(Target.MEMORY)) {
            return cpu.getMemoryValue(targetAddress);
        }
        return target.getValue(cpu);
    }
}
