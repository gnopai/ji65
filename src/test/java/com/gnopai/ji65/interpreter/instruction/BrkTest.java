package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.Address;
import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.interpreter.Operand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BrkTest {
    @Test
    void test() {
        byte processorStatus = (byte) 0b11000000;
        Cpu cpu = Cpu.builder()
                .programCounter(0x8124)
                .processorStatus(processorStatus)
                .build();
        cpu.setMemoryValue(new Address(0xFFFE), (byte) 0x9A);
        cpu.setMemoryValue(new Address(0xFFFF), (byte) 0x85);

        Operand operand = Operand.builder().build();

        new Brk().run(cpu, operand);

        assertEquals(0x859A, cpu.getProgramCounter());
        assertTrue(cpu.isBreakCommandSet());
        assertEquals(processorStatus, cpu.pullFromStack());
        assertEquals((byte) 0x24, cpu.pullFromStack());
        assertEquals((byte) 0x81, cpu.pullFromStack());
    }
}