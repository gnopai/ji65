package com.gnopai.ji65.interpreter;

import com.gnopai.ji65.Address;
import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.Program;

import java.util.List;

public class Interpreter {
    private final InstructionExecutor instructionExecutor;

    public Interpreter(InstructionExecutor instructionExecutor) {
        this.instructionExecutor = instructionExecutor;
    }

    public void run(Program program, Cpu cpu) {
        // TODO start executing instructions from a given address (or label)
        // TODO stop? how to know when to stop?
        List<Byte> programBytes = program.getBytes();
        Address startAddress = program.getMemoryAddress();
        cpu.copyToMemory(startAddress, programBytes);
        cpu.setProgramCounter(startAddress.getValue());

        // FIXME this is a temporary workaround to get the program to end
        byte nextValue = cpu.getMemoryValue(new Address(cpu.getProgramCounter()));
        while (nextValue != 0) {
            instructionExecutor.execute(cpu);
            nextValue = cpu.getMemoryValue(new Address(cpu.getProgramCounter()));
        }
    }
}
