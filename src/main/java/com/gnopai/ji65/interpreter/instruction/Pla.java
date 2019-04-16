package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.InstructionType;
import com.gnopai.ji65.interpreter.Operand;

public class Pla implements Instruction {
    @Override
    public InstructionType getInstructionType() {
        return InstructionType.PLA;
    }

    @Override
    public void run(Cpu cpu, Operand operand) {
        byte value = cpu.pullFromStack();
        cpu.setAccumulator(value);
        cpu.updateZeroFlag(value);
        cpu.updateNegativeFlag(value);
    }
}
