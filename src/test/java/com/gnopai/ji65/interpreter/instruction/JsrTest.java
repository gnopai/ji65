package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.Address;
import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.interpreter.Operand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JsrTest {
    @Test
    void test() {
        Cpu cpu = Cpu.builder()
                .programCounter(0x8124)
                .build();
        Address destinationAddress = new Address(0x83A9);
        Operand operand = Operand.of(destinationAddress);

        new Jsr().run(cpu, operand);

        assertEquals(0x83A9, cpu.getProgramCounter());
        assertEquals((byte) 0x23, cpu.pullFromStack()); // 1 should be subtracted
        assertEquals((byte) 0x81, cpu.pullFromStack());
    }

    @Test
    void testPageBoundary() {
        Cpu cpu = Cpu.builder()
                .programCounter(0x8100)
                .build();
        Address destinationAddress = new Address(0x83A9);
        Operand operand = Operand.of(destinationAddress);

        new Jsr().run(cpu, operand);

        assertEquals(0x83A9, cpu.getProgramCounter());
        assertEquals((byte) 0xFF, cpu.pullFromStack()); // 1 should be subtracted
        assertEquals((byte) 0x80, cpu.pullFromStack());
    }
}