package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.interpreter.Operand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DexTest {
    @Test
    void testNoFlags() {
        Cpu cpu = Cpu.builder()
                .x((byte) 0x34)
                .build();
        Operand operand = Operand.builder().build();

        new Dex().run(cpu, operand);

        assertEquals((byte) 0x33, cpu.getX());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testOverflowWithNegativeFlagSet() {
        Cpu cpu = Cpu.builder()
                .x((byte) 0x0)
                .build();
        Operand operand = Operand.builder().build();

        new Dex().run(cpu, operand);

        assertEquals((byte) 0xFF, cpu.getX());
        assertFalse(cpu.isZeroFlagSet());
        assertTrue(cpu.isNegativeFlagSet());
    }

    @Test
    void testZeroFlagSet() {
        Cpu cpu = Cpu.builder()
                .x((byte) 0x01)
                .build();
        Operand operand = Operand.builder().build();

        new Dex().run(cpu, operand);

        assertEquals((byte) 0x0, cpu.getX());
        assertTrue(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }
}
