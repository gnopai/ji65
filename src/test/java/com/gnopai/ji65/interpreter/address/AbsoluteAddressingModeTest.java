package com.gnopai.ji65.interpreter.address;

import com.gnopai.ji65.Address;
import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.interpreter.Operand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AbsoluteAddressingModeTest {

    @Test
    void test() {
        byte addressLowByte = (byte) 0x55;
        byte addressHighByte = (byte) 0x99;
        Cpu cpu = Cpu.builder().build();
        cpu.setProgramCounter(50);
        cpu.setMemoryValue(new Address(50), addressLowByte);
        cpu.setMemoryValue(new Address(51), addressHighByte);
        AbsoluteAddressingMode testClass = new AbsoluteAddressingMode();

        Operand result = testClass.determineRuntimeOperand(cpu);

        Operand expectedResult = Operand.builder()
                .highByte(addressHighByte)
                .lowByte(addressLowByte)
                .address(true)
                .build();
        assertEquals(expectedResult, result);
    }
}