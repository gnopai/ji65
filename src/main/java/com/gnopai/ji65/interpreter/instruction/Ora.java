package com.gnopai.ji65.interpreter.instruction;

import com.gnopai.ji65.InstructionType;

public class Ora extends LogicalInstruction {
    public Ora() {
        super(InstructionType.ORA, (a, b) -> (byte) (a | b));
    }
}
