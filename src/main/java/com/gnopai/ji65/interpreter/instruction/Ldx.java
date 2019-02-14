package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.Cpu;

import static com.gnopai.ji65.InstructionType.LDX;

public class Ldx extends LoadInstruction {
    public Ldx() {
        super(LDX, Cpu::setX);
    }
}
