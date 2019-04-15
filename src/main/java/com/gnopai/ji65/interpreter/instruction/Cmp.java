package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.InstructionType;

public class Cmp extends CompareInstruction {
    public Cmp() {
        super(InstructionType.CMP, Cpu::getAccumulator);
    }
}
