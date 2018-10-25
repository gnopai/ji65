package com.gnopai.ji65.instruction;

import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.Operand;

public interface Instruction {
    InstructionType getInstructionType();
    void run(Cpu cpu, Operand operand);
}
