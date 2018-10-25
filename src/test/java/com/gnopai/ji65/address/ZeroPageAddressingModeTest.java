package com.gnopai.ji65.address;

import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.Operand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ZeroPageAddressingModeTest {

    @Test
    public void test() {
        byte addressLowByte = (byte) 0x55;
        Operand operand = Operand.builder()
                .lowByte(addressLowByte)
                .build();
        Cpu cpu = Cpu.builder().build();
        ZeroPageAddressingMode testClass = new ZeroPageAddressingMode();

        Operand result = testClass.determineRuntimeOperand(cpu, operand);

        Operand expectedResult = Operand.builder()
                .lowByte(addressLowByte)
                .highByte((byte) 0x00)
                .address(true)
                .build();
        assertEquals(expectedResult, result);
    }
}
