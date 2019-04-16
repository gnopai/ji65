package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.Address;
import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.interpreter.Operand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RorTest {

    @Test
    void testAccumulator_carryFlagSetOnOutput() {
        byte value = (byte) 0b01100011;
        Cpu cpu = Cpu.builder()
                .accumulator(value)
                .build();
        Operand operand = Operand.builder()
                .lowByte(value)
                .build();

        new Ror().run(cpu, operand);

        assertEquals((byte) 0b00110001, cpu.getAccumulator());
        assertTrue(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testAccumulator_carryFlagSetOnInput() {
        byte value = (byte) 0b01100010;
        Cpu cpu = Cpu.builder()
                .accumulator(value)
                .build();
        cpu.setCarryFlag(true);
        Operand operand = Operand.builder()
                .lowByte(value)
                .build();

        new Ror().run(cpu, operand);

        assertEquals((byte) 0b10110001, cpu.getAccumulator());
        assertFalse(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertTrue(cpu.isNegativeFlagSet());
    }

    @Test
    void testAccumulator_carryFlagSetOnInputAndOutput() {
        byte value = (byte) 0b01100011;
        Cpu cpu = Cpu.builder()
                .accumulator(value)
                .build();
        cpu.setCarryFlag(true);
        Operand operand = Operand.builder()
                .lowByte(value)
                .build();

        new Ror().run(cpu, operand);

        assertEquals((byte) 0b10110001, cpu.getAccumulator());
        assertTrue(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertTrue(cpu.isNegativeFlagSet());
    }

    @Test
    void testAccumulator_carryFlagNotSet() {
        byte value = (byte) 0b01100010;
        Cpu cpu = Cpu.builder()
                .accumulator(value)
                .build();
        Operand operand = Operand.builder()
                .lowByte(value)
                .build();

        new Ror().run(cpu, operand);

        assertEquals((byte) 0b00110001, cpu.getAccumulator());
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

        new Ror().run(cpu, operand);

        assertEquals((byte) 0b00000000, cpu.getAccumulator());
        assertFalse(cpu.isCarryFlagSet());
        assertTrue(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testAccumulator_zeroFlagAndCarryFlagSet() {
        byte value = (byte) 0b00000001;
        Cpu cpu = Cpu.builder()
                .accumulator(value)
                .build();
        Operand operand = Operand.builder()
                .lowByte(value)
                .build();

        new Ror().run(cpu, operand);

        assertEquals((byte) 0b00000000, cpu.getAccumulator());
        assertTrue(cpu.isCarryFlagSet());
        assertTrue(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testMemoryAddress() {
        Address address = new Address(0x4567);
        byte value = (byte) 0b01010101;
        Cpu cpu = Cpu.builder().build();
        cpu.setMemoryValue(address, value);
        Operand operand = Operand.of(address);

        new Ror().run(cpu, operand);

        assertEquals((byte) 0b00101010, cpu.getMemoryValue(address));
        assertTrue(cpu.isCarryFlagSet());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }
}