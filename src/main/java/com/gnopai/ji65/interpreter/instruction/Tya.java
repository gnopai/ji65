package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.InstructionType;

public class Tya extends TransferInstruction {
    public Tya() {
        super(InstructionType.TYA, Cpu::getY, Cpu::setAccumulator);
    }
}
