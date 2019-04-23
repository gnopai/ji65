package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.InstructionType;

public class Bpl extends BranchInstruction {
    public Bpl() {
        super(InstructionType.BPL, Cpu::isNegativeFlagClear);
    }
}
