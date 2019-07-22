package com.gnopai.ji65.interpreter;

import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.Program;

import javax.inject.Inject;

public class Interpreter {
    private final InstructionExecutor instructionExecutor;

    @Inject
    public Interpreter(InstructionExecutor instructionExecutor) {
        this.instructionExecutor = instructionExecutor;
    }

    public void run(Program program, Cpu cpu, ProgramEndStrategy programEndStrategy) {
        program.getChunks().forEach(chunk ->
                cpu.copyToMemory(chunk.getAddress(), chunk.getBytes())
        );
        cpu.setProgramCounter(program.getStartAddress().getValue());

        while (!programEndStrategy.shouldEndProgram(cpu)) {
            instructionExecutor.execute(cpu);
        }
    }
}
