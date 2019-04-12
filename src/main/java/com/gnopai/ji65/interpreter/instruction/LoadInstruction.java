package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.InstructionType;
import com.gnopai.ji65.interpreter.Operand;

import java.util.function.BiConsumer;

public class LoadInstruction implements Instruction {
    private final OperandResolver operandResolver;
    private final InstructionType instructionType;
    private final BiConsumer<Cpu, Byte> cpuSetFunction;

    public LoadInstruction(InstructionType instructionType, BiConsumer<Cpu, Byte> cpuSetFunction) {
        this.operandResolver = new OperandResolver();
        this.instructionType = instructionType;
        this.cpuSetFunction = cpuSetFunction;
    }

    @Override
    public InstructionType getInstructionType() {
        return instructionType;
    }

    @Override
    public void run(Cpu cpu, Operand operand) {
        byte value = operandResolver.resolveOperand(cpu, operand);
        cpuSetFunction.accept(cpu, value);
        cpu.updateZeroFlag(value);
        cpu.updateNegativeFlag(value);
    }
}
