package com.gnopai.ji65.test;

import com.gnopai.ji65.assembler.Environment;
import com.gnopai.ji65.parser.statement.TestStatement;

import java.util.function.BiFunction;
import java.util.function.Function;

public enum TestStepType {
    SET(testMaker -> testMaker::makeSetValueStep),
    RUN(testMaker -> testMaker::makeRunSubRoutineStep),
    ASSERT(testMaker -> testMaker::makeAssertionStep),
    MOCK(testMaker -> testMaker::makeMockMemoryStep),
    VERIFY_READ(testMaker -> testMaker::makeVerifyReadStep),
    VERIFY_WRITE(testMaker -> testMaker::makeVerifyWriteStep),
    ;

    private final Function<TestMaker, BiFunction<TestStatement, Environment, TestStep>> testMakerFunction;

    TestStepType(Function<TestMaker, BiFunction<TestStatement, Environment, TestStep>> testMakerFunction) {
        this.testMakerFunction = testMakerFunction;
    }

    public TestStep makeTestStep(TestMaker testMaker, TestStatement testStatement, Environment environment) {
        return testMakerFunction.apply(testMaker)
                .apply(testStatement, environment);
    }
}
