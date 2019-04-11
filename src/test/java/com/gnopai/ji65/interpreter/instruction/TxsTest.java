package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.interpreter.Operand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TxsTest {
    @Test
    void test() {
        byte value = (byte) 0xF5;
        Cpu cpu = Cpu.builder()
                .x(value)
                .stackPointer((byte) 0xFF)
                .build();

        new Txs().run(cpu, Operand.builder().build());

        assertEquals(value, cpu.getX());
        assertEquals(value, cpu.getStackPointer());
    }
}