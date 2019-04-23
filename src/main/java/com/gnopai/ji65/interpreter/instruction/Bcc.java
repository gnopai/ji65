package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.InstructionType;

public class Bcc extends BranchInstruction {
    public Bcc() {
        super(InstructionType.BCC, Cpu::isCarryFlagClear);
    }
}
