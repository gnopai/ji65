package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.Address;
import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.interpreter.Operand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BitTest {
    @Test
    void testNoFlagsSet() {
        Address address = new Address(0x1234);
        Operand operand = Operand.of(address);
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0xAA)
                .build();
        cpu.setMemoryValue(address, (byte) 0x22);

        new Bit().run(cpu, operand);

        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isOverflowFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testZeroFlagSet() {
        Address address = new Address(0x1234);
        Operand operand = Operand.of(address);
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0xAA)
                .build();
        cpu.setMemoryValue(address, (byte) 0x11);

        new Bit().run(cpu, operand);

        assertTrue(cpu.isZeroFlagSet());
        assertFalse(cpu.isOverflowFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testOverflowFlagSet() {
        Address address = new Address(0x1234);
        Operand operand = Operand.of(address);
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0xFF)
                .build();
        cpu.setMemoryValue(address, (byte) 0x40);

        new Bit().run(cpu, operand);

        assertFalse(cpu.isZeroFlagSet());
        assertTrue(cpu.isOverflowFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testNegativeFlagSet() {
        Address address = new Address(0x1234);
        Operand operand = Operand.of(address);
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0xFF)
                .build();
        cpu.setMemoryValue(address, (byte) 0x80);

        new Bit().run(cpu, operand);

        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isOverflowFlagSet());
        assertTrue(cpu.isNegativeFlagSet());
    }

    @Test
    void testAllFlagsSet() {
        Address address = new Address(0x1234);
        Operand operand = Operand.of(address);
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0x0F)
                .build();
        cpu.setMemoryValue(address, (byte) 0xC0);

        new Bit().run(cpu, operand);

        assertTrue(cpu.isZeroFlagSet());
        assertTrue(cpu.isOverflowFlagSet());
        assertTrue(cpu.isNegativeFlagSet());
    }
}