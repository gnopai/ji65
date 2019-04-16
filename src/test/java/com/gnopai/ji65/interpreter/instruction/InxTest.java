package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.interpreter.Operand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InxTest {
    @Test
    void testNoFlags() {
        Cpu cpu = Cpu.builder()
                .x((byte) 0x34)
                .build();
        Operand operand = Operand.builder().build();

        new Inx().run(cpu, operand);

        assertEquals((byte) 0x35, cpu.getX());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testNegativeFlagSet() {
        Cpu cpu = Cpu.builder()
                .x((byte) 0x7F)
                .build();
        Operand operand = Operand.builder().build();

        new Inx().run(cpu, operand);

        assertEquals((byte) 0x80, cpu.getX());
        assertFalse(cpu.isZeroFlagSet());
        assertTrue(cpu.isNegativeFlagSet());
    }

    @Test
    void testZeroFlagSet() {
        Cpu cpu = Cpu.builder()
                .x((byte) 0xFF)
                .build();
        Operand operand = Operand.builder().build();

        new Inx().run(cpu, operand);

        assertEquals((byte) 0x0, cpu.getX());
        assertTrue(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }
}