package com.gnopai.ji65.parser;

import com.gnopai.ji65.Opcode;
import lombok.Value;

@Value
public class UnresolvedInstruction {
    Opcode opcode;
    String argument;

    public int getByteCount() {
        return opcode.getByteCount();
    }
}
