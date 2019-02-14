package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.InstructionType;
import com.gnopai.ji65.interpreter.Operand;

import java.util.function.Function;

public class StoreInstruction implements Instruction {
    private final InstructionType instructionType;
    private final Function<Cpu, Byte> cpuGetFunction;

    public StoreInstruction(InstructionType instructionType, Function<Cpu, Byte> cpuGetFunction) {
        this.instructionType = instructionType;
        this.cpuGetFunction = cpuGetFunction;
    }

    @Override
    public InstructionType getInstructionType() {
        return instructionType;
    }

    @Override
    public void run(Cpu cpu, Operand operand) {
        cpu.setMemoryValue(operand.asAddress(), cpuGetFunction.apply(cpu));
    }
}
