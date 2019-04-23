package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.InstructionType;

public class Bvs extends BranchInstruction {
    public Bvs() {
        super(InstructionType.BVS, Cpu::isOverflowFlagSet);
    }
}
