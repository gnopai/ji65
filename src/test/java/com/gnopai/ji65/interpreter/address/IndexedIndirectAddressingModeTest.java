package com.gnopai.ji65.interpreter.address;

import com.gnopai.ji65.Address;
import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.interpreter.Operand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IndexedIndirectAddressingModeTest {
    // lda ($45,X)

    @Test
    void testNormal() {
        Cpu cpu = Cpu.builder().build();
        cpu.setX((byte) 5);
        cpu.setProgramCounter(0x33);
        cpu.setMemoryValue(new Address(0x33), (byte) 0x19); // zero-page address to resolve
        cpu.setMemoryValue(new Address(0x1E), (byte) 0x54); // resolved address low-byte
        cpu.setMemoryValue(new Address(0x1F), (byte) 0xCC); // resolved address high-byte

        IndexedIndirectAddressingMode testClass = new IndexedIndirectAddressingMode();
        Operand operand = testClass.determineRuntimeOperand(cpu);
        Operand expectedOperand = Operand.builder()
                .highByte((byte) 0xCC)
                .lowByte((byte) 0x54)
                .address(true)
                .build();
        assertEquals(expectedOperand, operand);
    }

    @Test
    void testZeroPageWrapAround() {
        Cpu cpu = Cpu.builder().build();
        cpu.setX((byte) 3);
        cpu.setProgramCounter(0x33);
        cpu.setMemoryValue(new Address(0x33), (byte) 0xFF); // zero-page address to resolve
        cpu.setMemoryValue(new Address(0x02), (byte) 0x54); // resolved address low-byte
        cpu.setMemoryValue(new Address(0x03), (byte) 0xCC); // resolved address high-byte

        IndexedIndirectAddressingMode testClass = new IndexedIndirectAddressingMode();
        Operand operand = testClass.determineRuntimeOperand(cpu);
        Operand expectedOperand = Operand.builder()
                .highByte((byte) 0xCC)
                .lowByte((byte) 0x54)
                .address(true)
                .build();
        assertEquals(expectedOperand, operand);
    }
}