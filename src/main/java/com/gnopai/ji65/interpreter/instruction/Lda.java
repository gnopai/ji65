package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.Cpu;

import static com.gnopai.ji65.InstructionType.LDA;

public class Lda extends LoadInstruction {
    public Lda() {
        super(LDA, Cpu::setAccumulator);
    }
}
