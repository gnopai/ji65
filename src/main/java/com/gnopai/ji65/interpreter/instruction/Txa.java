package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.InstructionType;

public class Txa extends TransferInstruction {
    public Txa() {
        super(InstructionType.TXA, Cpu::getX, Cpu::setAccumulator);
    }
}
