package com.gnopai.ji65.interpreter.address;

import com.gnopai.ji65.Address;
import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.interpreter.Operand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IndirectIndexedAddressingModeTest {

    @Test
    void testSamePage() {
        Cpu cpu = Cpu.builder().build();
        cpu.setY((byte) 5);
        cpu.setProgramCounter(0x33);
        cpu.setMemoryValue(new Address(0x33), (byte) 0x19); // zero-page address to resolve
        cpu.setMemoryValue(new Address(0x19), (byte) 0x54); // resolved address low-byte
        cpu.setMemoryValue(new Address(0x1A), (byte) 0xCC); // resolved address high-byte

        IndirectIndexedAddressingMode testClass = new IndirectIndexedAddressingMode();
        Operand operand = testClass.determineRuntimeOperand(cpu);
        Operand expectedOperand = Operand.builder()
                .highByte((byte) 0xCC)
                .lowByte((byte) 0x59)
                .address(true)
                .build();
        assertEquals(expectedOperand, operand);
    }

    @Test
    void testPageCrossed() {
        Cpu cpu = Cpu.builder().build();
        cpu.setY((byte) 5);
        cpu.setProgramCounter(0x33);
        cpu.setMemoryValue(new Address(0x33), (byte) 0x19); // zero-page address to resolve
        cpu.setMemoryValue(new Address(0x19), (byte) 0xFE); // resolved address low-byte
        cpu.setMemoryValue(new Address(0x1A), (byte) 0x45); // resolved address high-byte

        IndirectIndexedAddressingMode testClass = new IndirectIndexedAddressingMode();
        Operand operand = testClass.determineRuntimeOperand(cpu);
        Operand expectedOperand = Operand.builder()
                .highByte((byte) 0x46)
                .lowByte((byte) 0x03)
                .address(true)
                .build();
        assertEquals(expectedOperand, operand);
    }

    @Test
    void testPageCrossedFromZeroToOne() {
        Cpu cpu = Cpu.builder().build();
        cpu.setY((byte) 5);
        cpu.setProgramCounter(0x33);
        cpu.setMemoryValue(new Address(0x33), (byte) 0x19); // zero-page address to resolve
        cpu.setMemoryValue(new Address(0x19), (byte) 0xFE); // resolved address low-byte
        cpu.setMemoryValue(new Address(0x1A), (byte) 0x00); // resolved address high-byte

        IndirectIndexedAddressingMode testClass = new IndirectIndexedAddressingMode();
        Operand operand = testClass.determineRuntimeOperand(cpu);
        Operand expectedOperand = Operand.builder()
                .highByte((byte) 0x01)
                .lowByte((byte) 0x03)
                .address(true)
                .build();
        assertEquals(expectedOperand, operand);
    }
}