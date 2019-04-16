package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.InstructionType;
import com.gnopai.ji65.interpreter.Operand;

public class Php implements Instruction {
    @Override
    public InstructionType getInstructionType() {
        return InstructionType.PHP;
    }

    @Override
    public void run(Cpu cpu, Operand operand) {
        cpu.pushOntoStack(cpu.getProcessorStatus());
    }
}
