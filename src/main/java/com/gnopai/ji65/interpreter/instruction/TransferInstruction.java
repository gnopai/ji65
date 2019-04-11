package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.InstructionType;
import com.gnopai.ji65.interpreter.Operand;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class TransferInstruction implements Instruction {
    private final InstructionType instructionType;
    private final Function<Cpu, Byte> cpuGetFunction;
    private final BiConsumer<Cpu, Byte> cpuSetFunction;

    public TransferInstruction(InstructionType instructionType, Function<Cpu, Byte> cpuGetFunction, BiConsumer<Cpu, Byte> cpuSetFunction) {
        this.instructionType = instructionType;
        this.cpuGetFunction = cpuGetFunction;
        this.cpuSetFunction = cpuSetFunction;
    }

    @Override
    public InstructionType getInstructionType() {
        return instructionType;
    }

    @Override
    public void run(Cpu cpu, Operand operand) {
        byte value = cpuGetFunction.apply(cpu);
        cpuSetFunction.accept(cpu, value);
        cpu.updateZeroFlag(value);
        cpu.updateNegativeFlag(value);
    }
}
