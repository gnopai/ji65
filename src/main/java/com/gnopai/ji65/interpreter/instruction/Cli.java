package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.InstructionType;
import com.gnopai.ji65.interpreter.Operand;

public class Cli implements Instruction {
    @Override
    public InstructionType getInstructionType() {
        return InstructionType.CLI;
    }

    @Override
    public void run(Cpu cpu, Operand operand) {
        cpu.setInterruptDisableSet(false);
    }
}
