package com.gnopai.ji65.interpreter;

import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.Opcode;
import com.gnopai.ji65.interpreter.address.AddressingMode;
import com.gnopai.ji65.interpreter.address.AddressingModeFactory;
import com.gnopai.ji65.interpreter.instruction.Instruction;
import com.gnopai.ji65.interpreter.instruction.InstructionFactory;

import javax.inject.Inject;

public class InstructionExecutor {
    private final InstructionFactory instructionFactory;
    private final AddressingModeFactory addressingModeFactory;

    @Inject
    public InstructionExecutor(InstructionFactory instructionFactory, AddressingModeFactory addressingModeFactory) {
        this.instructionFactory = instructionFactory;
        this.addressingModeFactory = addressingModeFactory;
    }

    public void execute(Cpu cpu) {
        byte opcodeByte = cpu.nextProgramByte();
        Opcode opcode = Opcode.of(opcodeByte)
                .orElseThrow(() -> new RuntimeException(String.format("Unrecognized opcode 0x%02X", opcodeByte)));
        Instruction instruction = instructionFactory.get(opcode.getInstructionType());
        AddressingMode addressingMode = addressingModeFactory.get(opcode.getAddressingModeType());
        Operand operand = addressingMode.determineRuntimeOperand(cpu);
        instruction.run(cpu, operand);
    }
}
