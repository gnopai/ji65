package com.gnopai.ji65.address;

import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.Operand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ZeroPageXAddressingModeTest {

    @Test
    public void test() {
        byte addressLowByte = (byte) 0x55;
        Operand operand = Operand.builder()
                .lowByte(addressLowByte)
                .build();
        Cpu cpu = Cpu.builder()
                .x((byte) 0x25)
                .build();

        ZeroPageXAddressingMode testClass = new ZeroPageXAddressingMode();

        Operand result = testClass.determineRuntimeOperand(cpu, operand);

        Operand expectedResult = Operand.builder()
                .lowByte((byte) 0x7A)
                .highByte((byte) 0x00)
                .address(true)
                .build();
        assertEquals(expectedResult, result);
    }
}
