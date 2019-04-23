package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.InstructionType;

public class Bne extends BranchInstruction {
    public Bne() {
        super(InstructionType.BNE, Cpu::isZeroFlagClear);
    }
}
