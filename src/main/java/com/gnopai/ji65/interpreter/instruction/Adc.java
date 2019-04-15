package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.InstructionType;
import com.gnopai.ji65.interpreter.Operand;

import static java.lang.Byte.toUnsignedInt;

public class Adc implements Instruction {
    private final OperandResolver operandResolver = new OperandResolver();

    @Override
    public InstructionType getInstructionType() {
        return InstructionType.ADC;
    }

    @Override
    public void run(Cpu cpu, Operand operand) {
        int accumulatorValue = toUnsignedInt(cpu.getAccumulator());
        int operandValue = toUnsignedInt(operandResolver.resolveOperand(cpu, operand));
        int carryValue = cpu.isCarryFlagSet() ? 1 : 0;
        int sum = accumulatorValue + operandValue + carryValue;
        cpu.setCarryFlag(sum >= 256);

        byte resultByte = (byte) sum;
        cpu.setAccumulator(resultByte);
        cpu.updateNegativeFlag(resultByte);
        cpu.updateZeroFlag(resultByte);

        boolean overflowFlag = calculateOverflow(accumulatorValue, operandValue, toUnsignedInt(resultByte));
        cpu.setOverflowFlag(overflowFlag);
    }

    private boolean calculateOverflow(int firstValue, int secondValue, int result) {
        return ((firstValue ^ result) & (secondValue ^ result) & 0x80) == 0x80;
    }
}
