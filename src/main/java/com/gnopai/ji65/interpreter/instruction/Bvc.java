package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.InstructionType;

public class Bvc extends BranchInstruction {
    public Bvc() {
        super(InstructionType.BVC, Cpu::isOverflowFlagClear);
    }
}
