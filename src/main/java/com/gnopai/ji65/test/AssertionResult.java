package com.gnopai.ji65.test;

import lombok.Value;

@Value
public class AssertionResult {
    boolean successful;
    byte expected;
    byte actual;
}
