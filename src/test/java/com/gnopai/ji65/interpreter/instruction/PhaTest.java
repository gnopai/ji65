package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.Address;
import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.interpreter.Operand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PhaTest {
    @Test
    void test() {
        byte value = 0x45;
        Cpu cpu = Cpu.builder()
                .accumulator(value)
                .build();
        Operand operand = Operand.builder().build();

        new Pha().run(cpu, operand);

        assertEquals(value, cpu.getMemoryValue(new Address(0x01FF)));
        assertEquals(value, cpu.pullFromStack());
    }
}