package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.InstructionType;

public class Beq extends BranchInstruction {
    public Beq() {
        super(InstructionType.BEQ, Cpu::isZeroFlagSet);
    }
}
