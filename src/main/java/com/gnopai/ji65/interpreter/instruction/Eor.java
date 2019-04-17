package com.gnopai.ji65.interpreter.instruction;


import com.gnopai.ji65.InstructionType;

public class Eor extends LogicalInstruction {
    public Eor() {
        super(InstructionType.EOR, (a, b) -> (byte) (a ^ b));
    }
}
