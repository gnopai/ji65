package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.InstructionType;
import com.gnopai.ji65.interpreter.Operand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CompareInstructionTest {
    private final CompareInstruction compareInstruction = new CompareInstruction(InstructionType.CPX, Cpu::getX);

    @Test
    void testRegisterLessThanOperand() {
        Cpu cpu = Cpu.builder()
                .x((byte) 0x54)
                .build();
        Operand operand = Operand.builder()
                .lowByte((byte) 0x55)
                .build();
        compareInstruction.run(cpu, operand);

        assertFalse(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertTrue(cpu.isNegativeFlagSet());
    }

    @Test
    void testRegisterEqualToOperand() {
        Cpu cpu = Cpu.builder()
                .x((byte) 0x55)
                .build();
        Operand operand = Operand.builder()
                .lowByte((byte) 0x55)
                .build();
        compareInstruction.run(cpu, operand);

        assertTrue(cpu.isCarryFlagSet());
        assertTrue(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testRegisterGreaterThanOperand() {
        Cpu cpu = Cpu.builder()
                .x((byte) 0x56)
                .build();
        Operand operand = Operand.builder()
                .lowByte((byte) 0x55)
                .build();
        compareInstruction.run(cpu, operand);

        assertTrue(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }
}