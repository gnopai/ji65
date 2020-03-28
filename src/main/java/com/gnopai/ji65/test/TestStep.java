package com.gnopai.ji65.test;

import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.Program;

public interface TestStep {
    void run(TestRunner testRunner, Cpu cpu, Program program);
}
