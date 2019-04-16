package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.InstructionType;
import com.gnopai.ji65.interpreter.Operand;

public class Plp implements Instruction {
    @Override
    public InstructionType getInstructionType() {
        return InstructionType.PLP;
    }

    @Override
    public void run(Cpu cpu, Operand operand) {
        cpu.setProcessorStatus(cpu.pullFromStack());
    }
}
