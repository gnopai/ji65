package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.InstructionType;
import com.gnopai.ji65.interpreter.Operand;

import java.util.function.BiFunction;

public class LogicalInstruction implements Instruction {
    private final InstructionType instructionType;
    private final BiFunction<Byte, Byte, Byte> function;
    private final OperandResolver operandResolver;

    public LogicalInstruction(InstructionType instructionType, BiFunction<Byte, Byte, Byte> function) {
        this.instructionType = instructionType;
        this.function = function;
        this.operandResolver = new OperandResolver();
    }

    @Override
    public InstructionType getInstructionType() {
        return instructionType;
    }

    @Override
    public void run(Cpu cpu, Operand operand) {
        byte value = operandResolver.resolveOperand(cpu, operand);
        byte result = function.apply(cpu.getAccumulator(), value);
        cpu.setAccumulator(result);
        cpu.updateZeroFlag(result);
        cpu.updateNegativeFlag(result);
    }
}
