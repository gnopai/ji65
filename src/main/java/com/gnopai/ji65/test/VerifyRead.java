package com.gnopai.ji65.test;

import com.gnopai.ji65.Address;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class VerifyRead implements TestStep {
    Address address;
    int expectedCount;

    @Override
    public void run(TestRunner testRunner, TestableCpu cpu) {
        testRunner.runStep(cpu, this);
    }

    @Override
    public List<Address> getWatchedAddresses() {
        return List.of(address);
    }
}
