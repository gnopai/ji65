package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.InstructionType;
import com.gnopai.ji65.interpreter.Operand;

import static java.lang.Byte.toUnsignedInt;

public class Dec implements Instruction {
    private final OperandResolver operandResolver = new OperandResolver();

    @Override
    public InstructionType getInstructionType() {
        return InstructionType.DEC;
    }

    @Override
    public void run(Cpu cpu, Operand operand) {
        int value = toUnsignedInt(operandResolver.resolveOperand(cpu, operand));
        byte result = (byte) (value - 1);
        cpu.setMemoryValue(operand.asAddress(), result);
        cpu.updateZeroFlag(result);
        cpu.updateNegativeFlag(result);
    }
}
