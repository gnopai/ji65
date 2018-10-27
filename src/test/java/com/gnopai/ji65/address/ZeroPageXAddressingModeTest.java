package com.gnopai.ji65.address;

import com.gnopai.ji65.BytesValue;
import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.Operand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class ZeroPageXAddressingModeTest {

    @Test
    void test() {
        byte addressLowByte = (byte) 0x55;
        BytesValue argument = new BytesValue(addressLowByte);
        Cpu cpu = Cpu.builder()
                .x((byte) 0x25)
                .build();

        ZeroPageXAddressingMode testClass = new ZeroPageXAddressingMode();

        Operand result = testClass.determineRuntimeOperand(cpu, argument);

        Operand expectedResult = Operand.builder()
                .value(new BytesValue((byte) 0x7A))
                .address(true)
                .build();
        assertEquals(expectedResult, result);
    }
}
