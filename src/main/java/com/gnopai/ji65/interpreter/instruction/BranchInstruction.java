package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.InstructionType;
import com.gnopai.ji65.interpreter.Operand;

import java.util.function.Predicate;

public class BranchInstruction implements Instruction {
    private final InstructionType instructionType;
    private final Predicate<Cpu> condition;

    public BranchInstruction(InstructionType instructionType, Predicate<Cpu> condition) {
        this.instructionType = instructionType;
        this.condition = condition;
    }

    @Override
    public InstructionType getInstructionType() {
        return instructionType;
    }

    @Override
    public void run(Cpu cpu, Operand operand) {
        if (condition.test(cpu)) {
            int offset = (int) operand.getLowByte(); // 2's complement signed value
            int newProgramCounter = cpu.getProgramCounter() + offset;
            cpu.setProgramCounter(newProgramCounter);
        }
    }
}
