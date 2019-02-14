package com.gnopai.ji65.address;

import com.gnopai.ji65.Address;
import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.Operand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class ImmediateAddressingModeTest {

    @Test
    void test() {
        byte value = (byte) 0x9A;
        Cpu cpu = Cpu.builder().build();
        cpu.setProgramCounter(50);
        cpu.setMemoryValue(new Address(50), value);
        cpu.setMemoryValue(new Address(51), (byte) 77);

        ImmediateAddressingMode testClass = new ImmediateAddressingMode();

        Operand result = testClass.determineRuntimeOperand(cpu);

        Operand expectedResult = Operand.builder()
                .address(false)
                .lowByte(value)
                .build();
        assertEquals(expectedResult, result);
    }
}
