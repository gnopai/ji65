package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.InstructionType;
import com.gnopai.ji65.interpreter.Operand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BranchInstructionTest {
    private final BranchInstruction testClass = new BranchInstruction(InstructionType.BCS, Cpu::isCarryFlagSet);

    @Test
    void testConditionTrue_positiveOffset() {
        Cpu cpu = Cpu.builder()
                .programCounter(0x8125)
                .build();
        cpu.setCarryFlag(true);
        Operand operand = Operand.builder()
                .lowByte((byte) 0x40)
                .build();

        testClass.run(cpu, operand);

        assertEquals(0x8165, cpu.getProgramCounter());
    }

    @Test
    void testConditionTrue_negativeOffset() {
        Cpu cpu = Cpu.builder()
                .programCounter(0x8125)
                .build();
        cpu.setCarryFlag(true);
        Operand operand = Operand.builder()
                .lowByte((byte) 0xEE)
                .build();

        testClass.run(cpu, operand);

        assertEquals(0x8113, cpu.getProgramCounter());
    }

    @Test
    void testConditionFalse() {
        Cpu cpu = Cpu.builder()
                .programCounter(0x8125)
                .build();
        cpu.setCarryFlag(false);
        Operand operand = Operand.builder()
                .lowByte((byte) 0x40)
                .build();

        testClass.run(cpu, operand);

        assertEquals(0x8125, cpu.getProgramCounter());
    }
}