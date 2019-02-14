package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.Cpu;

import static com.gnopai.ji65.InstructionType.LDY;

public class Ldy extends LoadInstruction {
    public Ldy() {
        super(LDY, Cpu::setY);
    }
}
