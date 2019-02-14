package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.Cpu;

import static com.gnopai.ji65.InstructionType.STA;

public class Sta extends StoreInstruction {
    public Sta() {
        super(STA, Cpu::getAccumulator);
    }
}
