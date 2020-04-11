package com.gnopai.ji65.test;

import com.gnopai.ji65.Address;
import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.Program;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RunSubRoutine implements TestStep {
    Address address;

    @Override
    public void run(TestRunner testRunner, Cpu cpu, Program program) {
        testRunner.runStep(cpu, this, program);
    }
}
