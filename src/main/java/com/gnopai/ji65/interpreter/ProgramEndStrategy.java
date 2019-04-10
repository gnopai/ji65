package com.gnopai.ji65.interpreter;

import com.gnopai.ji65.Cpu;

public interface ProgramEndStrategy {
    boolean shouldEndProgram(Cpu cpu);
}
