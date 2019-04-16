package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.InstructionType;
import com.gnopai.ji65.interpreter.Operand;

import static java.lang.Byte.toUnsignedInt;

public class Ror implements Instruction {
    private final OperandResolver operandResolver = new OperandResolver();

    @Override
    public InstructionType getInstructionType() {
        return InstructionType.ROR;
    }

    @Override
    public void run(Cpu cpu, Operand operand) {
        int originalValue = toUnsignedInt(operandResolver.resolveOperand(cpu, operand));
        int newValue = originalValue >> 1;
        if (cpu.isCarryFlagSet()) {
            newValue += 0x80; // set bit 7
        }
        byte newValueByte = (byte) newValue;

        if (operand.isAddress()) {
            cpu.setMemoryValue(operand.asAddress(), newValueByte);
        } else {
            cpu.setAccumulator(newValueByte);
        }

        cpu.setCarryFlag(originalValue % 2 > 0); // old bit 0
        cpu.updateZeroFlag(newValueByte);
        cpu.updateNegativeFlag(newValueByte);
    }
}
