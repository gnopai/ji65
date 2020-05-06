package com.gnopai.ji65.test;

import com.gnopai.ji65.Address;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SetValue implements TestStep {
    Target target;
    Address targetAddress;
    int value;

    @Override
    public void run(TestRunner testRunner, TestableCpu cpu) {
        testRunner.runStep(cpu, this);
    }
}
