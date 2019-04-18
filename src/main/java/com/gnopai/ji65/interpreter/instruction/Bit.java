package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.InstructionType;
import com.gnopai.ji65.interpreter.Operand;

public class Bit implements Instruction {
    private final OperandResolver operandResolver = new OperandResolver();

    @Override
    public InstructionType getInstructionType() {
        return InstructionType.BIT;
    }

    @Override
    public void run(Cpu cpu, Operand operand) {
        byte memoryValue = operandResolver.resolveOperand(cpu, operand);
        cpu.setZeroFlag((memoryValue & cpu.getAccumulator()) == 0);
        cpu.setOverflowFlag((memoryValue & 0x40) > 0);
        cpu.setNegativeFlag((memoryValue & 0x80) > 0);
    }
}
