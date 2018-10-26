package com.gnopai.ji65.instruction;

import com.gnopai.ji65.Cpu;

import static com.gnopai.ji65.instruction.InstructionType.STY;

public class Sty extends StoreInstruction {
    public Sty() {
        super(STY, Cpu::getY);
    }
}
