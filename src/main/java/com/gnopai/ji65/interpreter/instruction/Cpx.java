package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.InstructionType;

public class Cpx extends CompareInstruction {
    public Cpx() {
        super(InstructionType.CPX, Cpu::getX);
    }
}
