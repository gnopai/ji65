package com.gnopai.ji65.test;

import com.gnopai.ji65.Address;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class MockMemory implements TestStep {
    Address address;
    List<Byte> values;

    @Override
    public void run(TestRunner testRunner, TestableCpu cpu) {
        testRunner.runStep(cpu, this);
    }
}
