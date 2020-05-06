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
        if (value == expectedValue) {
            testResultTracker.assertionPassed(value);
        } else {
            testResultTracker.assertionFailed(expectedValue, value);
        }
    }

    void runStep(TestableCpu cpu, RunSubRoutine runSubRoutine) {
        interpreter.run(runSubRoutine.getAddress(), cpu, new EndProgramAtRtsOnEmptyStack());
    }

    private Byte getValue(TestableCpu cpu, Target target, Address targetAddress) {
        if (target.equals(Target.MEMORY)) {
            return cpu.getMemoryValue(targetAddress);
        }
        return target.getValue(cpu);
    }
}
