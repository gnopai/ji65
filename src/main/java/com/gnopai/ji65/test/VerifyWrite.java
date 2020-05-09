package com.gnopai.ji65.test;

import com.gnopai.ji65.Address;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class VerifyWrite implements TestStep {
    Address address;
    List<Byte> expectedValues;

    @Override
    public void run(TestRunner testRunner, TestableCpu cpu) {
        testRunner.runStep(cpu, this);
    }

    @Override
    public List<Address> getWatchedAddresses() {
        return List.of(address);
    }
}
