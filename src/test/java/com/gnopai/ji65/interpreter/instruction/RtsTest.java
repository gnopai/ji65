package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.interpreter.Operand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RtsTest {

    @Test
    void test() {
        Cpu cpu = Cpu.builder()
                .programCounter(0x8123)
                .build();
        cpu.pushOntoStack((byte) 0x85); // high byte
        cpu.pushOntoStack((byte) 0xAB); // low byte

        Operand operand = Operand.builder().build();

        new Rts().run(cpu, operand);

        assertEquals(0x85AC, cpu.getProgramCounter());
    }

    @Test
    void testPageBoundary() {
        Cpu cpu = Cpu.builder()
                .programCounter(0x8123)
                .build();
        cpu.pushOntoStack((byte) 0x86); // high byte
        cpu.pushOntoStack((byte) 0xFF); // low byte

        Operand operand = Operand.builder().build();

        new Rts().run(cpu, operand);

        assertEquals(0x8700, cpu.getProgramCounter());
    }
}