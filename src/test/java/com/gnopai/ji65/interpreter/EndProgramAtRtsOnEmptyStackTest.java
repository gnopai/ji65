package com.gnopai.ji65.interpreter;

import com.gnopai.ji65.Address;
import com.gnopai.ji65.Cpu;
import org.junit.jupiter.api.Test;

import static com.gnopai.ji65.Opcode.JSR_ABSOLUTE;
import static com.gnopai.ji65.Opcode.RTS_IMPLICIT;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EndProgramAtRtsOnEmptyStackTest {

    @Test
    void testRtsAndTopOfStack() {
        Cpu cpu = Cpu.builder()
                .programCounter(0x6789)
                .stackPointer((byte) 0xFF)
                .build();
        cpu.setMemoryValue(new Address(0x6789), RTS_IMPLICIT.getOpcode());

        assertTrue(new EndProgramAtRtsOnEmptyStack().shouldEndProgram(cpu));
    }

    @Test
    void testRtsButNotTopOfStack() {
        Cpu cpu = Cpu.builder()
                .programCounter(0x6789)
                .stackPointer((byte) 0xFE)
                .build();
        cpu.setMemoryValue(new Address(0x6789), RTS_IMPLICIT.getOpcode());

        assertFalse(new EndProgramAtRtsOnEmptyStack().shouldEndProgram(cpu));
    }
    @Test
    void testTopOfStackButNotRts() {
        Cpu cpu = Cpu.builder()
                .programCounter(0x6789)
                .stackPointer((byte) 0xFF)
                .build();
        cpu.setMemoryValue(new Address(0x6789), JSR_ABSOLUTE.getOpcode());

        assertFalse(new EndProgramAtRtsOnEmptyStack().shouldEndProgram(cpu));
    }
    @Test
    void testNotRtsAndNotTopOfStack() {
        Cpu cpu = Cpu.builder()
                .programCounter(0x6789)
                .stackPointer((byte) 0xFC)
                .build();
        cpu.setMemoryValue(new Address(0x6789), JSR_ABSOLUTE.getOpcode());

        assertFalse(new EndProgramAtRtsOnEmptyStack().shouldEndProgram(cpu));
    }
    @Test
    void testNextByteNotInstruction() {
        Cpu cpu = Cpu.builder()
                .programCounter(0x6789)
                .stackPointer((byte) 0xFF)
                .build();
        cpu.setMemoryValue(new Address(0x6789), (byte) 0xFF);

        assertFalse(new EndProgramAtRtsOnEmptyStack().shouldEndProgram(cpu));
    }
}