package com.gnopai.ji65.interpreter;

import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.Opcode;

public class EndProgramAtRtsOnEmptyStack implements ProgramEndStrategy {
    @Override
    public boolean shouldEndProgram(Cpu cpu) {
        return nextInstructionIsRts(cpu) && atTopOfStack(cpu);
    }

    private boolean nextInstructionIsRts(Cpu cpu) {
        return Opcode.of(cpu.peekNextProgramByte())
                .filter(opcode -> opcode.equals(Opcode.RTS_IMPLICIT))
                .isPresent();
    }

    private boolean atTopOfStack(Cpu cpu) {
        return cpu.getStackPointer() == (byte) 0xFF;
    }
}
