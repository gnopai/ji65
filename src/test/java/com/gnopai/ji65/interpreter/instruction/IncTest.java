package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.Address;
import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.interpreter.Operand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IncTest {
    @Test
    void testNoFlags() {
        Address address = new Address(0x1234);
        Cpu cpu = Cpu.builder().build();
        cpu.setMemoryValue(address, (byte) 0x34);
        Operand operand = Operand.of(address);

        new Inc().run(cpu, operand);

        assertEquals((byte) 0x35, cpu.getMemoryValue(address));
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testNegativeFlagSet() {
        Address address = new Address(0x1234);
        Cpu cpu = Cpu.builder().build();
        cpu.setMemoryValue(address, (byte) 0x7F);
        Operand operand = Operand.of(address);

        new Inc().run(cpu, operand);

        assertEquals((byte) 0x80, cpu.getMemoryValue(address));
        assertFalse(cpu.isZeroFlagSet());
        assertTrue(cpu.isNegativeFlagSet());
    }

    @Test
    void testZeroFlagSet() {
        Address address = new Address(0x1234);
        Cpu cpu = Cpu.builder().build();
        cpu.setMemoryValue(address, (byte) 0xFF);
        Operand operand = Operand.of(address);

        new Inc().run(cpu, operand);

        assertEquals((byte) 0x0, cpu.getMemoryValue(address));
        assertTrue(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }
}