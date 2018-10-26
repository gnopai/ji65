package com.gnopai.ji65.instruction;

import com.gnopai.ji65.Cpu;

import static com.gnopai.ji65.instruction.InstructionType.LDY;

public class Ldy extends LoadInstruction {
    public Ldy() {
        super(LDY, Cpu::setY);
    }
}
