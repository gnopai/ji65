package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.Address;
import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.interpreter.Operand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AslTest {

    @Test
    void testAccumulator_carryFlagSet() {
        byte value = (byte) 0b10100011;
        Cpu cpu = Cpu.builder()
                .accumulator(value)
                .build();
        Operand operand = Operand.builder()
                .lowByte(value)
                .build();

        new Asl().run(cpu, operand);

        assertEquals((byte) 0b01000110, cpu.getAccumulator());
        assertTrue(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testAccumulator_carryFlagNotSet() {
        byte value = (byte) 0b00100010;
        Cpu cpu = Cpu.builder()
                .accumulator(value)
                .build();
        Operand operand = Operand.builder()
                .lowByte(value)
                .build();

        new Asl().run(cpu, operand);

        assertEquals((byte) 0b01000100, cpu.getAccumulator());
        assertFalse(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testAccumulator_zeroFlagSet() {
        byte value = (byte) 0b00000000;
        Cpu cpu = Cpu.builder()
                .accumulator(value)
                .build();
        Operand operand = Operand.builder()
                .lowByte(value)
                .build();

        new Asl().run(cpu, operand);

        assertEquals((byte) 0b00000000, cpu.getAccumulator());
        assertFalse(cpu.isCarryFlagSet());
        assertTrue(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testAccumulator_zeroFlagAndCarryFlagSet() {
        byte value = (byte) 0b10000000;
        Cpu cpu = Cpu.builder()
                .accumulator(value)
                .build();
        Operand operand = Operand.builder()
                .lowByte(value)
                .build();

        new Asl().run(cpu, operand);

        assertEquals((byte) 0b00000000, cpu.getAccumulator());
        assertTrue(cpu.isCarryFlagSet());
        assertTrue(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testAccumulator_negativeFlagSet() {
        byte value = (byte) 0b01000000;
        Cpu cpu = Cpu.builder()
                .accumulator(value)
                .build();
        Operand operand = Operand.builder()
                .lowByte(value)
                .build();

        new Asl().run(cpu, operand);

        assertEquals((byte) 0b10000000, cpu.getAccumulator());
        assertFalse(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertTrue(cpu.isNegativeFlagSet());
    }

    @Test
    void testAccumulator_negativeFlagAndCarryFlagSet() {
        byte value = (byte) 0b11000000;
        Cpu cpu = Cpu.builder()
                .accumulator(value)
                .build();
        Operand operand = Operand.builder()
                .lowByte(value)
                .build();

        new Asl().run(cpu, operand);

        assertEquals((byte) 0b10000000, cpu.getAccumulator());
        assertTrue(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertTrue(cpu.isNegativeFlagSet());
    }

    @Test
    void testMemoryAddress() {
        Address address = new Address(0x4567);
        byte value = (byte) 0b01010101;
        Cpu cpu = Cpu.builder().build();
        cpu.setMemoryValue(address, value);
        Operand operand = Operand.of(address);

        new Asl().run(cpu, operand);

        assertEquals((byte) 0b10101010, cpu.getMemoryValue(address));
        assertFalse(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertTrue(cpu.isNegativeFlagSet());
    }
}