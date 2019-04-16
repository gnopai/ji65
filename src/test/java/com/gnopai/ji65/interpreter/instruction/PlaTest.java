package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.interpreter.Operand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlaTest {
    @Test
    void testNoFlagsSet() {
        byte value = 0x45;
        Cpu cpu = Cpu.builder().build();
        cpu.pushOntoStack(value);
        Operand operand = Operand.builder().build();

        new Pla().run(cpu, operand);

        assertEquals(value, cpu.getAccumulator());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testZeroFlagSet() {
        byte value = 0x0;
        Cpu cpu = Cpu.builder().build();
        cpu.pushOntoStack(value);
        Operand operand = Operand.builder().build();

        new Pla().run(cpu, operand);

        assertEquals(value, cpu.getAccumulator());
        assertTrue(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testNegativeFlagSet() {
        byte value = (byte) 0x80;
        Cpu cpu = Cpu.builder().build();
        cpu.pushOntoStack(value);
        Operand operand = Operand.builder().build();

        new Pla().run(cpu, operand);

        assertEquals(value, cpu.getAccumulator());
        assertFalse(cpu.isZeroFlagSet());
        assertTrue(cpu.isNegativeFlagSet());
    }
}