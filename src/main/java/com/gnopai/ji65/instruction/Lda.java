package com.gnopai.ji65.instruction;

import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.Operand;

public class Lda implements Instruction {

    @Override
    public InstructionType getInstructionType() {
        return InstructionType.LDA;
    }

    @Override
    public void run(Cpu cpu, Operand operand) {
        byte value = determineValue(cpu, operand);
        cpu.setAccumulator(value);
    }

    private byte determineValue(Cpu cpu, Operand operand) {
        if (operand.isAddress()) {
            return cpu.getMemoryValue(operand.asAddress());
        }
        return operand.getLowByte();
    }
}
