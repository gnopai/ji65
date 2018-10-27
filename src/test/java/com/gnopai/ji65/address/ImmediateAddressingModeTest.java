package com.gnopai.ji65.address;

import com.gnopai.ji65.BytesValue;
import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.Operand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class ImmediateAddressingModeTest {

    @Test
    void test() {
        byte value = (byte) 0x9A;
        BytesValue argument = new BytesValue(value);
        Cpu cpu = Cpu.builder().build();
        ImmediateAddressingMode testClass = new ImmediateAddressingMode();

        Operand result = testClass.determineRuntimeOperand(cpu, argument);

        Operand expectedResult = Operand.builder()
                .address(false)
                .value(argument)
                .build();
        assertEquals(expectedResult, result);
    }

}
