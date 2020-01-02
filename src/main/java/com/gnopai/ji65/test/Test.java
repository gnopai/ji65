package com.gnopai.ji65.test;

import lombok.Value;

import java.util.List;

@Value
public class Test {
    String name;
    List<TestStep> steps;
}
