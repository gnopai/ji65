package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.InstructionType;
import com.gnopai.ji65.interpreter.Operand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LogicalInstructionTest {
    private final LogicalInstruction testClass = new LogicalInstruction(InstructionType.AND, (a, b) -> (byte) (a & b));

    @Test
    void testNoFlagsSet() {
        Operand operand = Operand.builder()
                .lowByte((byte) 0xFF)
                .build();
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0x77)
                .build();

        testClass.run(cpu, operand);

        assertEquals((byte) 0x77, cpu.getAccumulator());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testZeroFlagSet() {
        Operand operand = Operand.builder()
                .lowByte((byte) 0xFF)
                .build();
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0x00)
                .build();

        testClass.run(cpu, operand);

        assertEquals((byte) 0x0, cpu.getAccumulator());
        assertTrue(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testNegativeFlagSet() {
        Operand operand = Operand.builder()
                .lowByte((byte) 0xFF)
                .build();
        Cpu cpu = Cpu.builder()
                .accumulator((byte) 0xFF)
                .build();

        testClass.run(cpu, operand);

        assertEquals((byte) 0xFF, cpu.getAccumulator());
        assertFalse(cpu.isZeroFlagSet());
        assertTrue(cpu.isNegativeFlagSet());
    }
}