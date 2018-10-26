package com.gnopai.ji65.instruction;

import com.gnopai.ji65.Address;
import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.Operand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StoreInstructionTest {
    private final InstructionType instructionType = InstructionType.STX;
    private StoreInstruction testClass;

    @BeforeEach
    public void setUp() {
        testClass = new StoreInstruction(instructionType, Cpu::getX);
    }

    @Test
    public void test() {
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
