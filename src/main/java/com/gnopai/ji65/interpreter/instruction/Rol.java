package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.InstructionType;
import com.gnopai.ji65.interpreter.Operand;

import static java.lang.Byte.toUnsignedInt;

public class Rol implements Instruction {
    private final OperandResolver operandResolver = new OperandResolver();

    @Override
    public InstructionType getInstructionType() {
        return InstructionType.ROL;
    }

    @Override
    public void run(Cpu cpu, Operand operand) {
        int originalValue = toUnsignedInt(operandResolver.resolveOperand(cpu, operand));
        int newValue = (originalValue << 1) & 0xFF;
        if (cpu.isCarryFlagSet()) {
            newValue++;
        }
        byte newValueByte = (byte) newValue;

        if (operand.isAddress()) {
            cpu.setMemoryValue(operand.asAddress(), newValueByte);
        } else {
            cpu.setAccumulator(newValueByte);
        }

        cpu.setCarryFlag(originalValue >= 128); // old bit 7
        cpu.updateZeroFlag(newValueByte);
        cpu.updateNegativeFlag(newValueByte);
    }
}
