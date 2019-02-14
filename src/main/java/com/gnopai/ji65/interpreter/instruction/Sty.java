package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.Cpu;

import static com.gnopai.ji65.InstructionType.STY;

public class Sty extends StoreInstruction {
    public Sty() {
        super(STY, Cpu::getY);
    }
}
