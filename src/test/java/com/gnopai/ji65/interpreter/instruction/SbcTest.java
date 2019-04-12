package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.interpreter.Operand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SbcTest {

    @Test
    void testWithCarrySet_noFlagsChangedFromResult() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0x25)
                .build();
        cpu.setCarryFlag(true);
        Operand operand = operand(0x10);

        new Sbc().run(cpu, operand);

        assertEquals((byte) 0x15, cpu.getAccumulator());
        assertTrue(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testWithoutCarrySet_noFlagsChangedFromResult() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0x25)
                .build();
        Operand operand = operand(0x10);

        new Sbc().run(cpu, operand);

        assertEquals((byte) 0x14, cpu.getAccumulator());
        assertTrue(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testWithCarrySet_zeroFlagSet() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0)
                .build();
        cpu.setCarryFlag(true);
        Operand operand = operand(0);

        new Sbc().run(cpu, operand);

        assertEquals((byte) 0, cpu.getAccumulator());
        assertTrue(cpu.isCarryFlagSet());
        assertTrue(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testWithCarrySet_negativeFlagSet() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0x95)
                .build();
        cpu.setCarryFlag(true);
        Operand operand = operand(0x04);

        new Sbc().run(cpu, operand);

        assertEquals((byte) 0x91, cpu.getAccumulator());
        assertTrue(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertTrue(cpu.isNegativeFlagSet());
    }

    @Test
    void testWithCarrySet_carryFlagCleared() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0x20)
                .build();
        cpu.setCarryFlag(true);
        Operand operand = operand(0xD0);

        new Sbc().run(cpu, operand);

        assertEquals((byte) 0x50, cpu.getAccumulator());
        assertFalse(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testWithCarrySet_carryClearedAndNegativeFlagSet() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0x10)
                .build();
        cpu.setCarryFlag(true);
        Operand operand = operand(0x20);

        new Sbc().run(cpu, operand);

        assertEquals((byte) 0xF0, cpu.getAccumulator());
        assertFalse(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertTrue(cpu.isNegativeFlagSet());
    }

    @Test
    void testWithoutCarrySet_carryFlagKeptClear() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0x23)
                .build();
        Operand operand = operand(0x40);

        new Sbc().run(cpu, operand);

        assertEquals((byte) 0xE2, cpu.getAccumulator());
        assertFalse(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertTrue(cpu.isNegativeFlagSet());
    }

    @Test
    void testWithoutCarrySet_carryFlagKeptClearAndZeroFlagSet() {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0)
                .build();
        Operand operand = operand(0xFF);

        new Sbc().run(cpu, operand);

        assertEquals((byte) 0, cpu.getAccumulator());
        assertFalse(cpu.isCarryFlagSet());
        assertTrue(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    private Operand operand(int value) {
        return Operand.builder()
                .lowByte((byte) value)
                .build();
    }
}