package com.gnopai.ji65.interpreter.address;

import com.gnopai.ji65.AddressingModeType;
import com.gnopai.ji65.Cpu;
import com.gnopai.ji65.interpreter.Operand;

public class ImplicitAddressingMode implements AddressingMode {
    @Override
    public AddressingModeType getType() {
        return AddressingModeType.IMPLICIT;
    }

    @Override
    public Operand determineRuntimeOperand(Cpu cpu) {
        return Operand.builder().build();
    }
}
