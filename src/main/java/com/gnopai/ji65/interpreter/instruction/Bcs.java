package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.InstructionType;

public class Bcs extends BranchInstruction {
    public Bcs() {
        super(InstructionType.BCS, Cpu::isCarryFlagSet);
    }
}
