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
        Address startAddress = new Address(0);
        cpu.copyToMemory(startAddress, programBytes);
        cpu.setProgramCounter(startAddress.getValue());

        // FIXME obviously it doesn't really work to just linearly execute the given instructions, but it seems like a decent workaround until this thing gets more real
        Address endAddress = new Address(startAddress.getValue() + programBytes.size());
        while (cpu.getProgramCounter() < endAddress.getValue()) {
            instructionExecutor.execute(cpu);
        }
    }
}
