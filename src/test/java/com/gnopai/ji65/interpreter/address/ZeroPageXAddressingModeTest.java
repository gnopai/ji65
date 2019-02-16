package com.gnopai.ji65.interpreter.address;

import com.gnopai.ji65.Address;
import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.interpreter.Operand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class ZeroPageXAddressingModeTest {

    @Test
    void testNormal() {
        byte addressLowByte = (byte) 0x55;
        Cpu cpu = Cpu.builder()
                .x((byte) 0x25)
                .build();
        cpu.setProgramCounter(50);
        cpu.setMemoryValue(new Address(50), addressLowByte);
        cpu.setMemoryValue(new Address(51), (byte) 77);

        ZeroPageXAddressingMode testClass = new ZeroPageXAddressingMode();

        Operand result = testClass.determineRuntimeOperand(cpu);

        Operand expectedResult = Operand.builder()
                .lowByte((byte) 0x7A)
                .address(true)
                .build();
        assertEquals(expectedResult, result);
    }

    @Test
    void testWrapAround() {
        byte addressLowByte = (byte) 255;
        Cpu cpu = Cpu.builder()
                .x((byte) 5)
                .build();
        cpu.setProgramCounter(50);
        cpu.setMemoryValue(new Address(50), addressLowByte);
        cpu.setMemoryValue(new Address(51), (byte) 77);

        ZeroPageXAddressingMode testClass = new ZeroPageXAddressingMode();

        Operand result = testClass.determineRuntimeOperand(cpu);

        Operand expectedResult = Operand.builder()
                .lowByte((byte) 4)
                .address(true)
                .build();
        assertEquals(expectedResult, result);
    }
}
