package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.Address;
import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.interpreter.Operand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DecTest {
    @Test
    void testNoFlags() {
        Address address = new Address(0x1234);
        Cpu cpu = Cpu.builder().build();
        cpu.setMemoryValue(address, (byte) 0x34);
        Operand operand = Operand.of(address);

        new Dec().run(cpu, operand);

        assertEquals((byte) 0x33, cpu.getMemoryValue(address));
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testOverflowWithNegativeFlagSet() {
        Address address = new Address(0x1234);
        Cpu cpu = Cpu.builder().build();
        cpu.setMemoryValue(address, (byte) 0x0);
        Operand operand = Operand.of(address);

        new Dec().run(cpu, operand);

        assertEquals((byte) 0xFF, cpu.getMemoryValue(address));
        assertFalse(cpu.isZeroFlagSet());
        assertTrue(cpu.isNegativeFlagSet());
    }

    @Test
    void testZeroFlagSet() {
        Address address = new Address(0x1234);
        Cpu cpu = Cpu.builder().build();
        cpu.setMemoryValue(address, (byte) 0x01);
        Operand operand = Operand.of(address);

        new Dec().run(cpu, operand);

        assertEquals((byte) 0x0, cpu.getMemoryValue(address));
        assertTrue(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }
}
