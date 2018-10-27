package com.gnopai.ji65;

import com.gnopai.ji65.address.AddressingMode;
import com.gnopai.ji65.address.AddressingModeFactory;
import com.gnopai.ji65.instruction.Instruction;
import com.gnopai.ji65.instruction.InstructionFactory;

public class ProgramRunner {
    private final AddressingModeFactory addressingModeFactory;
    private final InstructionFactory instructionFactory;

    public ProgramRunner(AddressingModeFactory addressingModeFactory, InstructionFactory instructionFactory) {
        this.addressingModeFactory = addressingModeFactory;
        this.instructionFactory = instructionFactory;
    }

    public void run(Cpu cpu, Program program) {
        program.getInstructions()
                .forEach(instruction -> executeInstruction(cpu, instruction));
    }

    private void executeInstruction(Cpu cpu, RuntimeInstruction runtimeInstruction) {
        AddressingMode addressingMode = addressingModeFactory.get(runtimeInstruction.getAddressingModeType());
        Operand operand = addressingMode.determineRuntimeOperand(cpu, runtimeInstruction.getArgument());
        Instruction instruction = instructionFactory.get(runtimeInstruction.getInstructionType());
        instruction.run(cpu, operand);
    }
}
