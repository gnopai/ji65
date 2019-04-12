package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.interpreter.Operand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdcTest {

    @Test
    void testWithoutCarry_noFlagsFromResult() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0x25)
                .build();
        Operand operand = operand(0x10);

        new Adc().run(cpu, operand);

        assertEquals((byte) 0x35, cpu.getAccumulator());
        assertFalse(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testWithCarry_noFlagsFromResult() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0x25)
                .build();
        cpu.setCarryFlag(true);
        Operand operand = operand(0x10);

        new Adc().run(cpu, operand);

        assertEquals((byte) 0x36, cpu.getAccumulator());
        assertFalse(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testWithoutCarry_zeroFlagSet() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0)
                .build();
        Operand operand = operand(0);

        new Adc().run(cpu, operand);

        assertEquals((byte) 0, cpu.getAccumulator());
        assertFalse(cpu.isCarryFlagSet());
        assertTrue(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testWithoutCarry_negativeFlagSet() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0x35)
                .build();
        Operand operand = operand(0x55);

        new Adc().run(cpu, operand);

        assertEquals((byte) 0x8A, cpu.getAccumulator());
        assertFalse(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertTrue(cpu.isNegativeFlagSet());
    }

    @Test
    void testWithoutCarry_carryFlagSet() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0x90)
                .build();
        Operand operand = operand(0x80);

        new Adc().run(cpu, operand);

        assertEquals((byte) 0x10, cpu.getAccumulator());
        assertTrue(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testWithoutCarry_carryAndZeroFlagsSet() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0x90)
                .build();
        Operand operand = operand(0x70);

        new Adc().run(cpu, operand);

        assertEquals((byte) 0, cpu.getAccumulator());
        assertTrue(cpu.isCarryFlagSet());
        assertTrue(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testWithCarry_carryFlagSetAgain() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0x90)
                .build();
        cpu.setCarryFlag(true);
        Operand operand = operand(0x80);

        new Adc().run(cpu, operand);

        assertEquals((byte) 0x11, cpu.getAccumulator());
        assertTrue(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    private Operand operand(int value) {
        return Operand.builder()
                .lowByte((byte) value)
                .build();
    }
}