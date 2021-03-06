package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.Address;
import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.InstructionType;
import com.gnopai.ji65.interpreter.Operand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StoreInstructionTest {
    private final InstructionType instructionType = InstructionType.STX;
    private StoreInstruction testClass;

    @BeforeEach
    void setUp() {
        testClass = new StoreInstruction(instructionType, Cpu::getX);
    }

    @Test
    void test() {
        Address address = new Address(0x1234);
        byte registerValue = (byte) 0x24;
        Cpu cpu = Cpu.builder()
                .x(registerValue)
                .build();
        Operand operand = Operand.of(address);

        assertEquals((byte) 0x00, cpu.getMemoryValue(address));
        testClass.run(cpu, operand);
        assertEquals(registerValue, cpu.getMemoryValue(address));
    }
}
