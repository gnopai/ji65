package com.gnopai.ji65.instruction;

import com.gnopai.ji65.Address;
import com.gnopai.ji65.BytesValue;
import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.Operand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class LoadInstructionTest {
    private final InstructionType instructionType = InstructionType.LDA;
    private LoadInstruction testClass;

    @BeforeEach
    void setUp() {
        testClass = new LoadInstruction(instructionType, Cpu::setAccumulator);
    }

    @Test
    void testAddress() {
        Address address = new Address(0x1234);
        byte value = (byte) 0x24;
        Cpu cpu = Cpu.builder().build();
        cpu.setMemoryValue(address, value);
        Operand operand = Operand.of(address);

        testClass.run(cpu, operand);

        assertEquals(value, cpu.getAccumulator());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testImmediate() {
        Cpu cpu = Cpu.builder().build();
        byte value = (byte) 0x7F;
        Operand operand = Operand.builder()
                .value(new BytesValue(value))
                .address(false)
                .build();

        testClass.run(cpu, operand);

        assertEquals(value, cpu.getAccumulator());
        assertFalse(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testImmediate_zero() {
        Cpu cpu = Cpu.builder().build();
        byte value = (byte) 0x00;
        Operand operand = Operand.builder()
                .value(new BytesValue(value))
                .address(false)
                .build();

        testClass.run(cpu, operand);

        assertEquals(value, cpu.getAccumulator());
        assertTrue(cpu.isZeroFlagSet());
        assertFalse(cpu.isNegativeFlagSet());
    }

    @Test
    void testImmediate_negative() {
        Cpu cpu = Cpu.builder().build();
        byte value = (byte) 0x80;
        Operand operand = Operand.builder()
                .value(new BytesValue(value))
                .address(false)
                .build();

        testClass.run(cpu, operand);

        assertEquals(value, cpu.getAccumulator());
        assertFalse(cpu.isZeroFlagSet());
        assertTrue(cpu.isNegativeFlagSet());
    }

}
