package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.InstructionType;

public class Tay extends TransferInstruction {
    public Tay() {
        super(InstructionType.TAY, Cpu::getAccumulator, Cpu::setY);
    }
}
