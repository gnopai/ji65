package com.gnopai.ji65.interpreter.address;

import com.gnopai.ji65.Address;
import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.interpreter.Operand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RelativeAddressingModeTest {
    @Test
    void test() {
        byte value = (byte) 0x9A;
        int programCounter = 0x8010;
        Cpu cpu = Cpu.builder().build();
        cpu.setProgramCounter(programCounter);
        cpu.setMemoryValue(new Address(programCounter), value);

        RelativeAddressingMode testClass = new RelativeAddressingMode();

        Operand result = testClass.determineRuntimeOperand(cpu);

        Operand expectedResult = Operand.builder()
                .address(false)
                .lowByte(value)
                .build();
        assertEquals(expectedResult, result);
    }
}