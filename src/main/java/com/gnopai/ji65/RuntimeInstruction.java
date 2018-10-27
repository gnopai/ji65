package com.gnopai.ji65;

import com.gnopai.ji65.address.AddressingModeType;
import com.gnopai.ji65.instruction.InstructionType;
import lombok.Value;

@Value
public class RuntimeInstruction {
    Opcode opcode;
    BytesValue argument;

    public InstructionType getInstructionType() {
        return opcode.getInstructionType();
    }

    public AddressingModeType getAddressingModeType() {
        return opcode.getAddressingModeType();
    }
}
