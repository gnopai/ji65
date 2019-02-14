package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.Cpu;

import static com.gnopai.ji65.InstructionType.STX;

public class Stx extends StoreInstruction {
    public Stx() {
        super(STX, Cpu::getX);
    }
}
