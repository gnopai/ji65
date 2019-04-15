package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.interpreter.Operand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

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

    @ParameterizedTest
    @MethodSource("overflowCalculationArgumentsProvider")
    void testOverflowCalculation(int firstValue, int secondValue, boolean expectedOverflowFlag, String message) {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) firstValue)
                .build();
        cpu.setCarryFlag(true);
        Operand operand = operand(secondValue);

        new Sbc().run(cpu, operand);

        assertEquals(expectedOverflowFlag, cpu.isOverflowFlagSet(), message);
    }

    static Stream<Arguments> overflowCalculationArgumentsProvider() {
        // Note that C6 is flipped here: 0 means carry, 1 means no carry
        return Stream.of(
                arguments(0x00, 0xC0, false, "M7: 0, N7: 1, C6: 0"),
                arguments(0x00, 0x80, true, "M7: 0, N7: 1, C6: 1"),
                arguments(0x00, 0x40, false, "M7: 0, N7: 0, C6: 0"),
                arguments(0x00, 0x00, false, "M7: 0, N7: 0, C6: 1"),
                arguments(0x80, 0xC0, false, "M7: 1, N7: 1, C6: 0"),
                arguments(0x80, 0x80, false, "M7: 1, N7: 1, C6: 1"),
                arguments(0x80, 0x40, true, "M7: 1, N7: 0, C6: 0"),
                arguments(0x80, 0x00, false, "M7: 1, N7: 0, C6: 1")
        );
    }

    private Operand operand(int value) {
        return Operand.builder()
                .lowByte((byte) value)
                .build();
    }
}