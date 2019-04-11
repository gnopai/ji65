package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.InstructionType;

public class Tax extends TransferInstruction {
    public Tax() {
        super(InstructionType.TAX, Cpu::getAccumulator, Cpu::setX);
    }
}
