package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.InstructionType;

public class Cpy extends CompareInstruction {
    public Cpy() {
        super(InstructionType.CPY, Cpu::getY);
    }
}
