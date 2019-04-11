package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.InstructionType;
import com.gnopai.ji65.interpreter.Operand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransferInstructionTest {
    private final TransferInstruction testClass = new TransferInstruction(InstructionType.TAX, Cpu::getX, Cpu::setY);
    private final Operand operand = Operand.builder().build();

    @Test
    void testTransfer_noFlagsSet() {
        Cpu cpu = Cpu.builder()
                .x((byte) 1)
                .y((byte) 0)
                .build();
        testClass.run(cpu, operand);
        assertEquals((byte) 1, cpu.getX());
        assertEquals((byte) 1, cpu.getY());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testTransfer_zeroFlagSet() {
        Cpu cpu = Cpu.builder()
                .x((byte) 0)
                .y((byte) 1)
                .build();
        testClass.run(cpu, operand);
        assertEquals((byte) 0, cpu.getX());
        assertEquals((byte) 0, cpu.getY());
        assertTrue(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testTransfer_negativeFlagSet() {
        Cpu cpu = Cpu.builder()
                .x((byte) 0x80)
                .y((byte) 0)
                .build();
        testClass.run(cpu, operand);
        assertEquals((byte) 0x80, cpu.getX());
        assertEquals((byte) 0x80, cpu.getY());
        assertFalse(cpu.isZeroFlagSet());
        assertTrue(cpu.isNegativeFlagSet());
    }
}