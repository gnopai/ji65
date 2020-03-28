package com.gnopai.ji65.test;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class TestResult {
    String name;
    @Singular
    List<AssertionResult> assertionResults;
}
