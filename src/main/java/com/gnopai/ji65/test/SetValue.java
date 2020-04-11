package com.gnopai.ji65.test;

import com.gnopai.ji65.Address;
import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.Program;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SetValue implements TestStep {
    Target target;
    Address targetAddress;
    int value;

    @Override
    public void run(TestRunner testRunner, Cpu cpu, Program program) {
        testRunner.runStep(cpu, this);
    }
}
