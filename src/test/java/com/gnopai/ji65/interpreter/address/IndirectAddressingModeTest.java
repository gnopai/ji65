package com.gnopai.ji65.interpreter.address;

import com.gnopai.ji65.Address;
import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.interpreter.Operand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IndirectAddressingModeTest {

    @Test
    void testNormal() {
        Cpu cpu = Cpu.builder().build();
        cpu.setProgramCounter(0x8033);
        cpu.setMemoryValue(new Address(0x8033), (byte) 0x81); // indirect address low-byte
        cpu.setMemoryValue(new Address(0x8034), (byte) 0xA9); // indirect address high-byte
        cpu.setMemoryValue(new Address(0xA981), (byte) 0x54); // resolved address low-byte
        cpu.setMemoryValue(new Address(0xA982), (byte) 0xCC); // resolved address high-byte

        IndirectAddressingMode testClass = new IndirectAddressingMode();
        Operand operand = testClass.determineRuntimeOperand(cpu);
        Operand expectedOperand = Operand.builder()
                .highByte((byte) 0xCC)
                .lowByte((byte) 0x54)
                .address(true)
                .build();
        assertEquals(expectedOperand, operand);
    }

    @Test
    void testPageBoundary() {
        // Note that some earlier 6502 processors don't handle page boundaries correctly, and an indirect address for $82FF
        // would retrieve bytes at $82FF and $8200 instead of $82FF and $8300. The latter is implemented here, as it seems like
        // the generally desired behavior.
        Cpu cpu = Cpu.builder().build();
        cpu.setProgramCounter(0x8033);
        cpu.setMemoryValue(new Address(0x8033), (byte) 0xFF); // indirect address low-byte
        cpu.setMemoryValue(new Address(0x8034), (byte) 0x82); // indirect address high-byte
        cpu.setMemoryValue(new Address(0x82FF), (byte) 0x54); // resolved address low-byte
        cpu.setMemoryValue(new Address(0x8300), (byte) 0xCC); // resolved address high-byte

        IndirectAddressingMode testClass = new IndirectAddressingMode();
        Operand operand = testClass.determineRuntimeOperand(cpu);
        Operand expectedOperand = Operand.builder()
                .highByte((byte) 0xCC)
                .lowByte((byte) 0x54)
                .address(true)
                .build();
        assertEquals(expectedOperand, operand);
    }
}