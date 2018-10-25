package com.gnopai.ji65.address;

import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.Operand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ImmediateAddressingModeTest {

    @Test
    public void test() throws Exception {
        byte value = (byte) 0x9A;
        Operand operand = Operand.builder().lowByte(value).build();
        Cpu cpu = Cpu.builder().build();
        ImmediateAddressingMode testClass = new ImmediateAddressingMode();

        Operand result = testClass.determineRuntimeOperand(cpu, operand);

        Operand expectedResult = Operand.builder()
                .address(false)
                .lowByte(value)
                .build();
        assertEquals(expectedResult, result);
    }

}
