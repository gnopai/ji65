package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.InstructionType;
import com.gnopai.ji65.interpreter.Operand;

public interface Instruction {
    InstructionType getInstructionType();
    void run(Cpu cpu, Operand operand);
}
