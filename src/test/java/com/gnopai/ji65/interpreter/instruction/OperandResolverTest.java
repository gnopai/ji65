package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.Address;
import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.interpreter.Operand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OperandResolverTest {
    @Test
    void testAddress() {
        Address address = new Address(0x1234);
        byte value = (byte) 0xAB;
        Cpu cpu = Cpu.builder().build();
        cpu.setMemoryValue(address, value);

        Operand operand = Operand.builder()
                .highByte((byte) 0x12)
                .lowByte((byte) 0x34)
                .address(true)
                .build();
        byte result = new OperandResolver().resolveOperand(cpu, operand);

        assertEquals(value, result);
    }

    @Test
    void testImmediate() {
        byte value = (byte) 0xAB;
        Cpu cpu = Cpu.builder().build();

        Operand operand = Operand.builder()
                .lowByte(value)
                .build();
        byte result = new OperandResolver().resolveOperand(cpu, operand);

        assertEquals(value, result);
    }
}