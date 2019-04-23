package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.InstructionType;

public class Bmi extends BranchInstruction {
    public Bmi() {
        super(InstructionType.BMI, Cpu::isNegativeFlagSet);
    }
}
