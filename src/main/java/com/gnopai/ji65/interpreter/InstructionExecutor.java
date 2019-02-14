package com.gnopai.ji65.interpreter;

import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.Opcode;
import com.gnopai.ji65.Operand;
import com.gnopai.ji65.address.AddressingMode;
import com.gnopai.ji65.address.AddressingModeFactory;
import com.gnopai.ji65.instruction.Instruction;
import com.gnopai.ji65.instruction.InstructionFactory;

public class InstructionExecutor {
    private final InstructionFactory instructionFactory;
    private final AddressingModeFactory addressingModeFactory;

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
