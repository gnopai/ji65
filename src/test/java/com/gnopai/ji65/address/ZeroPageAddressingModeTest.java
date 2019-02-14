package com.gnopai.ji65.address;

import com.gnopai.ji65.Address;
import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.Operand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class ZeroPageAddressingModeTest {

    @Test
    void test() {
        byte addressLowByte = (byte) 0x55;
        Cpu cpu = Cpu.builder().build();
        cpu.setProgramCounter(50);
        cpu.setMemoryValue(new Address(50), addressLowByte);
        cpu.setMemoryValue(new Address(51), (byte) 77);
        ZeroPageAddressingMode testClass = new ZeroPageAddressingMode();

        Operand result = testClass.determineRuntimeOperand(cpu);

        Operand expectedResult = Operand.builder()
                .lowByte(addressLowByte)
                .address(true)
                .build();
        assertEquals(expectedResult, result);
    }
}
