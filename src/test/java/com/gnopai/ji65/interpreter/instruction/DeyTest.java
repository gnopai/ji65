package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.interpreter.Operand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeyTest {
    @Test
    void testNoFlags() {
        Cpu cpu = Cpu.builder()
                .y((byte) 0x34)
                .build();
        Operand operand = Operand.builder().build();

        new Dey().run(cpu, operand);

        assertEquals((byte) 0x33, cpu.getY());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testOverflowWithNegativeFlagSet() {
        Cpu cpu = Cpu.builder()
                .y((byte) 0x0)
                .build();
        Operand operand = Operand.builder().build();

        new Dey().run(cpu, operand);

        assertEquals((byte) 0xFF, cpu.getY());
        assertFalse(cpu.isZeroFlagSet());
        assertTrue(cpu.isNegativeFlagSet());
    }

    @Test
    void testZeroFlagSet() {
        Cpu cpu = Cpu.builder()
                .y((byte) 0x01)
                .build();
        Operand operand = Operand.builder().build();

        new Dey().run(cpu, operand);

        assertEquals((byte) 0x0, cpu.getY());
        assertTrue(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }
}
