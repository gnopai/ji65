package com.gnopai.ji65.interpreter.address;

import com.gnopai.ji65.Address;
import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.interpreter.Operand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AbsoluteXAddressingModeTest {

    @Test
    void testSamePage() {
        byte addressLowByte = (byte) 0x55;
        byte addressHighByte = (byte) 0x99;
        Cpu cpu = Cpu.builder().build();
        cpu.setX((byte) 4);
        cpu.setProgramCounter(50);
        cpu.setMemoryValue(new Address(50), addressLowByte);
        cpu.setMemoryValue(new Address(51), addressHighByte);
        AbsoluteXAddressingMode testClass = new AbsoluteXAddressingMode();

        Operand result = testClass.determineRuntimeOperand(cpu);

        Operand expectedResult = Operand.builder()
                .highByte(addressHighByte)
                .lowByte((byte) 0x59)
                .address(true)
                .build();
        assertEquals(expectedResult, result);
    }

    @Test
    void testPageCrossed() {
        byte addressLowByte = (byte) 0xFF;
        byte addressHighByte = (byte) 0x98;
        Cpu cpu = Cpu.builder().build();
        cpu.setX((byte) 3);
        cpu.setProgramCounter(50);
        cpu.setMemoryValue(new Address(50), addressLowByte);
        cpu.setMemoryValue(new Address(51), addressHighByte);
        AbsoluteXAddressingMode testClass = new AbsoluteXAddressingMode();

        Operand result = testClass.determineRuntimeOperand(cpu);

        Operand expectedResult = Operand.builder()
                .highByte((byte) 0x99)
                .lowByte((byte) 0x02)
                .address(true)
                .build();
        assertEquals(expectedResult, result);
    }

    @Test
    void testPageCrossedFromZeroToOne() {
        byte addressLowByte = (byte) 0xFF;
        byte addressHighByte = (byte) 0x00;
        Cpu cpu = Cpu.builder().build();
        cpu.setX((byte) 3);
        cpu.setProgramCounter(50);
        cpu.setMemoryValue(new Address(50), addressLowByte);
        cpu.setMemoryValue(new Address(51), addressHighByte);
        AbsoluteXAddressingMode testClass = new AbsoluteXAddressingMode();

        Operand result = testClass.determineRuntimeOperand(cpu);

        Operand expectedResult = Operand.builder()
                .highByte((byte) 0x01)
                .lowByte((byte) 0x02)
                .address(true)
                .build();
        assertEquals(expectedResult, result);
    }
}