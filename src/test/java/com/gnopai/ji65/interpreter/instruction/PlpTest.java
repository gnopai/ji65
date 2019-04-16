package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.interpreter.Operand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PlpTest {
    @Test
    void testNoFlagsSet() {
        Cpu cpu = Cpu.builder()
                .processorStatus((byte) 0xFF)
                .build();
        byte newProcessorStatus = 0x45;
        cpu.pushOntoStack(newProcessorStatus);
        Operand operand = Operand.builder().build();

        new Plp().run(cpu, operand);

        assertEquals(newProcessorStatus, cpu.getProcessorStatus());
    }
}