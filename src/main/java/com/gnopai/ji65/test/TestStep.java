package com.gnopai.ji65.test;

import com.gnopai.ji65.Address;

import java.util.List;

public interface TestStep {
    void run(TestRunner testRunner, TestableCpu cpu);

    default List<Address> getWatchedAddresses() {
        return List.of();
    }
}
