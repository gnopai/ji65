package com.gnopai.ji65.interpreter;

import com.gnopai.ji65.Address;
import com.gnopai.ji65.Cpu;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Interpreter {
    private final InstructionExecutor instructionExecutor;

    @Inject
    public Interpreter(InstructionExecutor instructionExecutor) {
        this.instructionExecutor = instructionExecutor;
    }

    public void run(Address startAddress, Cpu cpu, ProgramEndStrategy programEndStrategy) {
        cpu.setProgramCounter(startAddress.getValue());
        while (!programEndStrategy.shouldEndProgram(cpu)) {
            instructionExecutor.execute(cpu);
        }
    }
}
