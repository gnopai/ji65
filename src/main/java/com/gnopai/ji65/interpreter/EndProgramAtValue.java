package com.gnopai.ji65.interpreter;

import com.gnopai.ji65.Address;
import com.gnopai.ji65.Cpu;

public class EndProgramAtValue implements ProgramEndStrategy {
    private final byte endValue;

    public EndProgramAtValue(byte endValue) {
        this.endValue = endValue;
    }

    @Override
    public boolean shouldEndProgram(Cpu cpu) {
        byte currentValue = cpu.getMemoryValue(new Address(cpu.getProgramCounter()));
        return currentValue == endValue;
    }
}
