package com.gnopai.ji65.interpreter;

import com.gnopai.ji65.Address;
import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.Opcode;
import com.gnopai.ji65.interpreter.address.AddressingModeFactory;
import com.gnopai.ji65.interpreter.instruction.InstructionFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InstructionExecutorTest {

    @Test
    void testExecute() {
        Address address = new Address(0x8051);
        Cpu cpu = Cpu.builder().build();
        byte argumentValue = (byte) 15;
        cpu.setMemoryValue(address, Opcode.LDX_IMMEDIATE.getOpcode());
        cpu.setMemoryValue(address.plus(1), argumentValue);
        cpu.setProgramCounter(address);

        InstructionExecutor instructionExecutor = new InstructionExecutor(new InstructionFactory(), new AddressingModeFactory());
        instructionExecutor.execute(cpu);

        assertEquals(argumentValue, cpu.getX());
        assertEquals(address.plus(2).getValue(), cpu.getProgramCounter());
    }

    @Test
    void testBadOpcode() {
        Address address = new Address(0x8051);
        Cpu cpu = Cpu.builder().build();
        cpu.setMemoryValue(address, (byte) 0xFF);
        cpu.setProgramCounter(address);

        InstructionExecutor instructionExecutor = new InstructionExecutor(new InstructionFactory(), new AddressingModeFactory());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> instructionExecutor.execute(cpu));
        assertEquals("Unrecognized opcode 0xFF", exception.getMessage());
    }
}