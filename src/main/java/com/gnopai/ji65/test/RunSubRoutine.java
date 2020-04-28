package com.gnopai.ji65.test;

import com.gnopai.ji65.Address;
import com.gnopai.ji65.Cpu;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RunSubRoutine implements TestStep {
    Address address;

    @Override
    public void run(TestRunner testRunner, Cpu cpu) {
        testRunner.runStep(cpu, this);
    }
}
