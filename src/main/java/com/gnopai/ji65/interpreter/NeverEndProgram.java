package com.gnopai.ji65.interpreter;

import com.gnopai.ji65.Cpu;

public class NeverEndProgram implements ProgramEndStrategy {
    @Override
    public boolean shouldEndProgram(Cpu cpu) {
        return false;
    }
}
