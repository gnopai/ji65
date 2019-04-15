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

    @ParameterizedTest
    @MethodSource("overflowCalculationArgumentsProvider")
    void testOverflowCalculation(int firstValue, int secondValue, boolean expectedOverflowFlag, String message) {
        Cpu cpu = Cpu.builder()
                .accumulator((byte) firstValue)
                .build();
        Operand operand = operand(secondValue);

        new Adc().run(cpu, operand);

        assertEquals(expectedOverflowFlag, cpu.isOverflowFlagSet(), message);
    }

    static Stream<Arguments> overflowCalculationArgumentsProvider() {
        return Stream.of(
                arguments(0x00, 0x00, false, "M7: 0, N7: 0, C6: 0"),
                arguments(0x40, 0x40, true, "M7: 0, N7: 0, C6: 1"),
                arguments(0x00, 0x80, false, "M7: 0, N7: 1, C6: 0"),
                arguments(0x40, 0xC0, false, "M7: 0, N7: 1, C6: 1"),
                arguments(0x80, 0x00, false, "M7: 1, N7: 0, C6: 0"),
                arguments(0xC0, 0x40, false, "M7: 1, N7: 0, C6: 1"),
                arguments(0x80, 0x80, true, "M7: 1, N7: 1, C6: 0"),
                arguments(0xC0, 0xC0, false, "M7: 1, N7: 1, C6: 1")
        );
    }

    private Operand operand(int value) {
        return Operand.builder()
                .lowByte((byte) value)
                .build();
    }
}