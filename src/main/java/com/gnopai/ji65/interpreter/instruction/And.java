package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.InstructionType;

public class And extends LogicalInstruction {
    public And() {
        super(InstructionType.AND, (a, b) -> (byte) (a & b));
    }
}
