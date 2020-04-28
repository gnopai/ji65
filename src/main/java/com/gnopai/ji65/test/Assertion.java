package com.gnopai.ji65.test;

import com.gnopai.ji65.Address;
import com.gnopai.ji65.Cpu;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Assertion implements TestStep {
    Target target;
    Address targetAddress;
    int expectedValue;
    String message;

    @Override
    public void run(TestRunner testRunner, Cpu cpu) {
        testRunner.runStep(cpu, this);
    }
}
