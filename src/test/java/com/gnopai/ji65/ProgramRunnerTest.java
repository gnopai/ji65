package com.gnopai.ji65;

import com.gnopai.ji65.address.AddressingMode;
import com.gnopai.ji65.address.AddressingModeFactory;
import com.gnopai.ji65.instruction.Instruction;
import com.gnopai.ji65.instruction.InstructionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.gnopai.ji65.Opcode.*;
import static org.mockito.Mockito.*;

class ProgramRunnerTest {
    private final AddressingModeFactory addressingModeFactory = mock(AddressingModeFactory.class);
    private final InstructionFactory instructionFactory = mock(InstructionFactory.class);
    private ProgramRunner testClass;

    @BeforeEach
    void setUp() {
        testClass = new ProgramRunner(addressingModeFactory, instructionFactory);
    }

    @Test
    void test() {
        // GIVEN
        Cpu cpu = Cpu.builder().accumulator((byte) 0x77).build();

        BytesValue instructionArgument1 = new BytesValue((byte) 0x01);
        RuntimeInstruction runtimeInstruction1 = new RuntimeInstruction(LDA_ZERO_PAGE, instructionArgument1);
        Instruction instruction1 = mock(Instruction.class);
        Operand operand1 = new Operand(instructionArgument1, true);
        mockUpInstruction(cpu, instruction1, runtimeInstruction1, operand1);

        BytesValue instructionArgument2 = new BytesValue((byte) 0x04);
        RuntimeInstruction runtimeInstruction2 = new RuntimeInstruction(STA_ZERO_PAGE_X, instructionArgument2);
        Instruction instruction2 = mock(Instruction.class);
        Operand operand2 = new Operand(instructionArgument2, true);
        mockUpInstruction(cpu, instruction2, runtimeInstruction2, operand2);

        BytesValue instructionArgument3 = new BytesValue((byte) 0x08);
        RuntimeInstruction runtimeInstruction3 = new RuntimeInstruction(LDX_ABSOLUTE, instructionArgument3);
        Instruction instruction3 = mock(Instruction.class);
        Operand operand3 = new Operand(instructionArgument3, true);
        mockUpInstruction(cpu, instruction3, runtimeInstruction3, operand3);

        Program program = new Program(List.of(
                runtimeInstruction1,
                runtimeInstruction2,
                runtimeInstruction3
        ));

        // WHEN
        testClass.run(cpu, program);

        // THEN
        verify(instruction1).run(cpu, operand1);
        verify(instruction2).run(cpu, operand2);
        verify(instruction3).run(cpu, operand3);
    }

    private void mockUpInstruction(Cpu cpu, Instruction instruction, RuntimeInstruction runtimeInstruction, Operand operand) {
        Opcode opcode = runtimeInstruction.getOpcode();
        AddressingMode addressingMode = mock(AddressingMode.class);
        when(addressingModeFactory.get(opcode.getAddressingModeType())).thenReturn(addressingMode);
        when(addressingMode.determineRuntimeOperand(cpu, runtimeInstruction.getArgument())).thenReturn(operand);
        when(instructionFactory.get(opcode.getInstructionType())).thenReturn(instruction);
    }
}