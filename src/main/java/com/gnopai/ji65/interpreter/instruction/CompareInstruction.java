package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.InstructionType;
import com.gnopai.ji65.interpreter.Operand;

import java.util.function.Function;

import static java.lang.Byte.toUnsignedInt;

public class CompareInstruction implements Instruction {
    private final InstructionType instructionType;
    private final Function<Cpu, Byte> getValueFunction;
    private final OperandResolver operandResolver;

    public CompareInstruction(InstructionType instructionType, Function<Cpu, Byte> getValueFunction) {
        this.instructionType = instructionType;
        this.getValueFunction = getValueFunction;
        this.operandResolver = new OperandResolver();
    }

    @Override
    public InstructionType getInstructionType() {
        return instructionType;
    }

    @Override
    public void run(Cpu cpu, Operand operand) {
        int registerValue = toUnsignedInt(getValueFunction.apply(cpu));
        int operandValue = toUnsignedInt(operandResolver.resolveOperand(cpu, operand));
        cpu.setCarryFlag(registerValue >= operandValue);
        cpu.setZeroFlag(registerValue == operandValue);
        cpu.setNegativeFlag(registerValue < operandValue);
    }
}
