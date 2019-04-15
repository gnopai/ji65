package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.InstructionType;
import com.gnopai.ji65.interpreter.Operand;

import static java.lang.Byte.toUnsignedInt;

public class Sbc implements Instruction {
    private final OperandResolver operandResolver = new OperandResolver();

    @Override
    public InstructionType getInstructionType() {
        return InstructionType.SBC;
    }

    @Override
    public void run(Cpu cpu, Operand operand) {
        int accumulatorValue = toUnsignedInt(cpu.getAccumulator());
        int operandValue = toUnsignedInt(operandResolver.resolveOperand(cpu, operand));
        int carryValue = cpu.isCarryFlagSet() ? 0 : 1;
        int difference = accumulatorValue - operandValue - carryValue;
        cpu.setCarryFlag(difference >= 0);

        byte resultByte = (byte) (difference + 0x100);
        cpu.setAccumulator(resultByte);
        cpu.updateNegativeFlag(resultByte);
        cpu.updateZeroFlag(resultByte);

        boolean overflowFlag = calculateOverflow(accumulatorValue, operandValue, toUnsignedInt(resultByte));
        cpu.setOverflowFlag(overflowFlag);
    }

    private boolean calculateOverflow(int firstValue, int secondValue, int result) {
        int secondValueComplement = 255 - secondValue;
        return ((firstValue ^ result) & (secondValueComplement ^ result) & 0x80) == 0x80;
    }
}
