package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.interpreter.Operand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RtiTest {

    @Test
    void test() {
        Cpu cpu = Cpu.builder()
                .processorStatus((byte) 0x10101010)
                .programCounter(0x8123)
                .build();

        byte storedProcessorStatus = (byte) 0b01010100;

        cpu.pushOntoStack((byte) 0x85); // high byte
        cpu.pushOntoStack((byte) 0xAB); // low byte
        cpu.pushOntoStack(storedProcessorStatus);

        Operand operand = Operand.builder().build();

        new Rti().run(cpu, operand);

        assertEquals(storedProcessorStatus, cpu.getProcessorStatus());
        assertEquals(0x85AB, cpu.getProgramCounter());
    }
}