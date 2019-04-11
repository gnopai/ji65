package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.InstructionType;

public class Tsx extends TransferInstruction {
    public Tsx() {
        super(InstructionType.TSX, Cpu::getStackPointer, Cpu::setX);
    }
}
