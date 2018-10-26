package com.gnopai.ji65.instruction;

import com.gnopai.ji65.Cpu;

import static com.gnopai.ji65.instruction.InstructionType.STX;

public class Stx extends StoreInstruction {
    public Stx() {
        super(STX, Cpu::getX);
    }
}
